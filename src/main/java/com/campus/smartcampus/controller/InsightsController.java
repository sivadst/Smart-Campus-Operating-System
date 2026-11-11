package com.campus.smartcampus.controller;

import com.campus.smartcampus.service.ai.AttendanceRiskPredictor;
import com.campus.smartcampus.service.ai.InsightsService;
import com.campus.smartcampus.service.ai.NoticePrioritizer;
import com.campus.smartcampus.service.ai.RoomRecommender;
import com.campus.smartcampus.service.ai.SecurityAnalyser;
import com.campus.smartcampus.service.ai.TimetableSuggester;
import com.campus.smartcampus.service.ai.dataset.DataSeeder;
import com.campus.smartcampus.service.ai.dataset.Records;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * Simple REST endpoints exposing the 6 AI algorithms as JSON.
 * Run with: java -cp out com.campus.smartcampus.controller.InsightsController
 * Then browse: http://localhost:8081/api/v1/ai/*
 */
public final class InsightsController {

    private static final int PORT = 8081;

    private InsightsController() {}

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.setExecutor(Executors.newCachedThreadPool());

        // CORS + JSON headers for all endpoints
        server.createContext("/api/v1/ai/attendance-risk", new JsonHandler(() -> {
            List<Records.Attendance> history = DataSeeder.historyFor("S1");
            String risk = AttendanceRiskPredictor.risk(history);
            double pct = AttendanceRiskPredictor.attendancePercentage(history);
            String explain = AttendanceRiskPredictor.explain(history);
            return Map.of("studentId", "S1", "risk", risk, "percentage", pct, "explanation", explain);
        }));

        server.createContext("/api/v1/ai/timetable-slots", new JsonHandler(() -> {
            Records.Course course = DataSeeder.demoCourse();
            List<Records.Room> rooms = DataSeeder.rooms();
            List<Records.TimetableEntry> existing = DataSeeder.timetable();
            List<TimetableSuggester.SuggestedSlot> slots = TimetableSuggester.suggest(course, DataSeeder.DEMO_FACULTY_ID, rooms, existing, 5);
            return slots.stream().map(s -> Map.of(
                "day", s.day().toString(),
                "start", s.start().toString(),
                "end", s.end().toString(),
                "roomId", s.roomId(),
                "reason", s.reason()
            )).toList();
        }));

        server.createContext("/api/v1/ai/room-recommendations", new JsonHandler(() -> {
            Records.Course course = DataSeeder.demoCourse();
            List<Records.Room> rooms = DataSeeder.rooms();
            List<Records.RoomBooking> bookings = DataSeeder.bookings();
            List<RoomRecommender.RoomScore> recs = RoomRecommender.recommend(course, true, rooms, bookings,
                    DataSeeder.CS301_SLOT_START, DataSeeder.CS301_SLOT_END, 5);
            return recs.stream().map(r -> Map.of(
                "roomId", r.room().id(),
                "roomCode", r.room().code(),
                "capacity", r.room().capacity(),
                "hasProjector", r.room().hasProjector(),
                "hasAc", r.room().hasAc(),
                "hasWifi", r.room().hasWifi(),
                "score", r.score(),
                "reason", r.reason()
            )).toList();
        }));

        server.createContext("/api/v1/ai/notices", new JsonHandler(() -> {
            List<Records.Notice> notices = DataSeeder.notices();
            List<NoticePrioritizer.ScoredNotice> ranked = NoticePrioritizer.rank(notices, DataSeeder.DEMO_NOW, "STUDENT", 10);
            return ranked.stream().map(sn -> Map.of(
                "noticeId", sn.notice().id(),
                "title", sn.notice().title(),
                "category", sn.notice().category().name(),
                "pinned", sn.notice().pinned(),
                "published", sn.notice().published(),
                "score", sn.score(),
                "reason", sn.reason()
            )).toList();
        }));

        server.createContext("/api/v1/ai/security-findings", new JsonHandler(() -> {
            List<Records.LoginEvent> events = DataSeeder.loginEvents();
            List<SecurityAnalyser.Finding> findings = SecurityAnalyser.flag(events);
            return findings.stream().map(f -> Map.of(
                "severity", f.severity(),
                "type", f.type(),
                "subject", f.subject(),
                "detail", f.detail()
            )).toList();
        }));

        server.createContext("/api/v1/ai/insights", new JsonHandler(() -> {
            InsightsService.CampusInsights ci = InsightsService.compute(
                    DataSeeder.students(),
                    DataSeeder.attendance(),
                    DataSeeder.rooms(),
                    DataSeeder.bookings(),
                    DataSeeder.timetable(),
                    DataSeeder.notices()
            );
            return Map.of(
                "averageAttendance", ci.averageAttendance(),
                "highRiskStudents", ci.highRiskStudents(),
                "roomUtilisationPct", ci.roomUtilisationPct(),
                "noticesByCategory", ci.noticesByCategory(),
                "topRooms", ci.topRooms()
            );
        }));

        // Health check
        server.createContext("/api/v1/ai/health", new JsonHandler(() -> Map.of("status", "UP")));

        // Static file serving for demo.html
        server.createContext("/", new StaticFileHandler());

        server.start();
        System.out.println("AI REST server running at http://localhost:" + PORT + "/api/v1/ai/*");
        System.out.println("Endpoints:");
        System.out.println("  GET /api/v1/ai/attendance-risk");
        System.out.println("  GET /api/v1/ai/timetable-slots");
        System.out.println("  GET /api/v1/ai/room-recommendations");
        System.out.println("  GET /api/v1/ai/notices");
        System.out.println("  GET /api/v1/ai/security-findings");
        System.out.println("  GET /api/v1/ai/insights");
        System.out.println("  GET /api/v1/ai/health");
    }

    @FunctionalInterface
    interface JsonSupplier {
        Object get() throws Exception;
    }

    static class JsonHandler implements HttpHandler {
        private final JsonSupplier supplier;

        JsonHandler(JsonSupplier supplier) {
            this.supplier = supplier;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                send(exchange, 405, Map.of("error", "Method not allowed"));
                return;
            }
            try {
                Object result = supplier.get();
                String json = toJson(result);
                send(exchange, 200, json);
            } catch (Exception e) {
                e.printStackTrace();
                send(exchange, 500, Map.of("error", e.getMessage()));
            }
        }

        private void send(HttpExchange exchange, int code, Object body) throws IOException {
            String json = body instanceof String ? (String) body : toJson(body);
            byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, OPTIONS");
            exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
            exchange.sendResponseHeaders(code, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }

        // Minimal JSON serializer (no external deps)
        private String toJson(Object o) {
            if (o == null) return "null";
            if (o instanceof String) return "\"" + escape((String) o) + "\"";
            if (o instanceof Number || o instanceof Boolean) return o.toString();
            if (o instanceof List<?> list) {
                return "[" + list.stream().map(this::toJson).reduce((a, b) -> a + "," + b).orElse("") + "]";
            }
            if (o instanceof Map<?, ?> map) {
                return "{" + map.entrySet().stream()
                        .map(e -> "\"" + escape(e.getKey().toString()) + "\":" + toJson(e.getValue()))
                        .reduce((a, b) -> a + "," + b).orElse("") + "}";
            }
            return "\"" + escape(o.toString()) + "\"";
        }

        private String escape(String s) {
            return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
        }
    }

    static class StaticFileHandler implements HttpHandler {
        private static final Path STATIC_DIR = Paths.get("src/main/resources/static").toAbsolutePath();

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendError(exchange, 405, "Method not allowed");
                return;
            }

            String path = exchange.getRequestURI().getPath();
            if (path.equals("/") || path.equals("")) {
                path = "/demo.html";
            }

            Path file = STATIC_DIR.resolve(path.substring(1)).normalize();
            if (!file.startsWith(STATIC_DIR)) {
                sendError(exchange, 403, "Forbidden");
                return;
            }

            if (!Files.exists(file) || Files.isDirectory(file)) {
                sendError(exchange, 404, "Not found");
                return;
            }

            String mime = Files.probeContentType(file);
            if (mime == null) {
                mime = "application/octet-stream";
            }

            byte[] bytes = Files.readAllBytes(file);
            exchange.getResponseHeaders().set("Content-Type", mime + "; charset=UTF-8");
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }

        private void sendError(HttpExchange exchange, int code, String msg) throws IOException {
            String body = "{\"error\":\"" + msg + "\"}";
            byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            exchange.sendResponseHeaders(code, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
    }
}