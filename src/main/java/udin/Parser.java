package udin;

public class Parser {
    public static boolean isList(String cmd) { return "list".equals(cmd); }
    public static boolean isBye(String cmd) { return "bye".equals(cmd); }
    public static boolean isMark(String cmd) { return cmd.startsWith("mark "); }
    public static boolean isUnmark(String cmd) { return cmd.startsWith("unmark "); }
    public static boolean isTodo(String cmd) { return cmd.startsWith("todo "); }
    public static boolean isDeadline(String cmd) { return cmd.startsWith("deadline "); }
    public static boolean isEvent(String cmd) { return cmd.startsWith("event "); }
    public static boolean isDelete(String cmd) { return cmd.startsWith("delete "); }

    public static int parseIndex(String cmd) {
        return Integer.parseInt(cmd.split(" ")[1]) - 1;
    }

    public static String[] parseDeadlineParts(String input) {
        String[] parts = input.substring(9).split("/by", 2);
        return new String[]{ parts[0].trim(), parts.length > 1 ? parts[1].trim() : "" };
    }

    public static String[] parseEventParts(String input) {
        String[] parts = input.substring(6).split("/from|/to");
        for (int i = 0; i < parts.length; i++) parts[i] = parts[i].trim();
        return parts;
    }
}
