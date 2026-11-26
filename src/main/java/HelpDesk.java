import com.sun.net.httpserver.*;
import java.net.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.io.*;
import java.util.*;

public class HelpDesk {
    record Ticket(String id, String title, String description, String priority, String status) {}
    static final Path DATA = Path.of("data", "tickets.tsv");
    static final Path WEB = Path.of("src/main/resources/web");
    static final Map<String, Ticket> tickets = new LinkedHashMap<>();
    static final Set<String> priorities = Set.of("Baja", "Media", "Alta");
    static final Set<String> statuses = Set.of("Abierta", "En curso", "Resuelta");
    public static void main(String[] args) throws IOException {
        load();
        HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 8080), 0);
        server.createContext("/api/tickets", HelpDesk::api);
        server.createContext("/", HelpDesk::web);
        server.start();
        System.out.println("IT HelpDesk: http://localhost:8080 (Ctrl+C para detener)");
    }
    static String encode(String s) { return Base64.getEncoder().encodeToString(s.getBytes(StandardCharsets.UTF_8)); }
    static String decode(String s) { return new String(Base64.getDecoder().decode(s), StandardCharsets.UTF_8); }
    static void load() throws IOException {
        if (!Files.exists(DATA)) return;
        for (String line : Files.readAllLines(DATA)) {
            String[] a = line.split("\t", -1);
            if (a.length == 5) tickets.put(a[0], new Ticket(a[0], decode(a[1]), decode(a[2]), a[3], a[4]));
        }
    }
    static void save(Map<String, Ticket> next) throws IOException {
        Files.createDirectories(DATA.getParent());
        List<String> lines = new ArrayList<>();
        for (Ticket t : next.values()) lines.add(String.join("\t", t.id(), encode(t.title()), encode(t.description()), t.priority(), t.status()));
        Path temp = DATA.resolveSibling("tickets.tmp");
        Files.write(temp, lines);
        Files.move(temp, DATA, StandardCopyOption.REPLACE_EXISTING);
        tickets.clear(); tickets.putAll(next);
    }
    static String q(String s) {
        StringBuilder b = new StringBuilder("\"");
        for (char c : s.toCharArray()) {
            if (c == '"' || c == '\\') b.append('\\').append(c);
            else if (c < 32) b.append(String.format("\\u%04x", (int)c));
            else b.append(c);
        }
        return b.append('"').toString();
    }
    static String json(Ticket t) {
        return "{\"id\":"+q(t.id())+",\"title\":"+q(t.title())+",\"description\":"+q(t.description())+",\"priority\":"+q(t.priority())+",\"status\":"+q(t.status())+"}";
    }
    static Map<String,String> form(HttpExchange x) throws IOException {
        byte[] body = x.getRequestBody().readNBytes(16385);
        if (body.length > 16384) throw new IllegalArgumentException("Petición demasiado grande");
        Map<String,String> m = new HashMap<>();
        for (String pair : new String(body, StandardCharsets.UTF_8).split("&")) {
            String[] a = pair.split("=",2);
            if (a.length==2) m.put(URLDecoder.decode(a[0], StandardCharsets.UTF_8), URLDecoder.decode(a[1], StandardCharsets.UTF_8));
        }
        return m;
    }
    static void send(HttpExchange x, int code, String type, byte[] bytes) throws IOException {
        x.getResponseHeaders().set("Content-Type", type);
        x.getResponseHeaders().set("X-Content-Type-Options", "nosniff");
        x.sendResponseHeaders(code, bytes.length);
        try (var out = x.getResponseBody()) { out.write(bytes); }
    }
    static void reply(HttpExchange x,int code,String s) throws IOException { send(x,code,"application/json; charset=utf-8",s.getBytes(StandardCharsets.UTF_8)); }
    static void error(HttpExchange x,int code,String s) throws IOException { reply(x,code,"{\"error\":"+q(s)+"}"); }
    static synchronized void api(HttpExchange x) throws IOException {
        try {
            String path=x.getRequestURI().getPath(), method=x.getRequestMethod();
            String base="/api/tickets";
            Map<String,Ticket> next=new LinkedHashMap<>(tickets);
            if (path.equals(base)) {
                if (method.equals("GET")) { reply(x,200,"["+String.join(",",tickets.values().stream().map(HelpDesk::json).toList())+"]"); return; }
                if (!method.equals("POST")) { error(x,405,"Método no permitido"); return; }
                var m=form(x); String title=m.getOrDefault("title","").strip(), desc=m.getOrDefault("description","").strip(), priority=m.getOrDefault("priority","");
                if(title.isEmpty() || title.length()>100 || desc.isEmpty() || desc.length()>1000 || !priorities.contains(priority)) throw new IllegalArgumentException("Datos de incidencia no válidos");
                Ticket t=new Ticket(UUID.randomUUID().toString(),title,desc,priority,"Abierta"); next.put(t.id(),t); save(next); reply(x,201,json(t)); return;
            }
            if (!path.startsWith(base+"/")) { error(x,404,"Ruta no encontrada"); return; }
            String id=path.substring(base.length()+1); Ticket t=tickets.get(id);
            if(t==null) { error(x,404,"Incidencia no encontrada"); return; }
            if(method.equals("PATCH")) {
                String status=form(x).getOrDefault("status","");
                if(!statuses.contains(status)) throw new IllegalArgumentException("Estado no válido");
                Ticket updated=new Ticket(id,t.title(),t.description(),t.priority(),status); next.put(id,updated); save(next); reply(x,200,json(updated));
            } else if(method.equals("DELETE")) { next.remove(id); save(next); reply(x,200,"{\"ok\":true}"); }
            else error(x,405,"Método no permitido");
        } catch(IllegalArgumentException e) { error(x,400,e.getMessage()); }
        catch(IOException e) { error(x,500,"Error de almacenamiento"); }
        finally { x.close(); }
    }
    static void web(HttpExchange x) throws IOException {
        try {
            if(!x.getRequestMethod().equals("GET")) { error(x,405,"Método no permitido"); return; }
            String name=switch(x.getRequestURI().getPath()) { case "/", "/index.html" -> "index.html"; case "/style.css" -> "style.css"; case "/app.js" -> "app.js"; default -> null; };
            if(name==null) { error(x,404,"Ruta no encontrada"); return; }
            String type=name.endsWith(".html")?"text/html":name.endsWith(".css")?"text/css":"text/javascript";
            send(x,200,type+"; charset=utf-8",Files.readAllBytes(WEB.resolve(name)));
        } finally { x.close(); }
    }
}
