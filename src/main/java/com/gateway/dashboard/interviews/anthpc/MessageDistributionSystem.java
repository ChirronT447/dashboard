package com.gateway.dashboard.interviews.anthpc;

import java.util.List;

import java.util.Objects;

public class MessageDistributionSystem {

    public record ServerNode(String id) {}
    public record Message(String dialogueId, String content) {}

    public static class MessageDistributor {
        private final List<ServerNode> servers;

        public MessageDistributor(List<ServerNode> servers) {
            if (servers == null || servers.isEmpty()) {
                throw new IllegalArgumentException("Server list cannot be null or empty.");
            }
            // Create an immutable copy for safety
            this.servers = List.copyOf(servers);
        }

        /**
         * Routes a message to a server node based on its dialogue ID's hash code.
         * @param message The message to route.
         * @return The determined ServerNode.
         */
        public ServerNode route(Message message) {
            Objects.requireNonNull(message, "Message cannot be null");
            Objects.requireNonNull(message.dialogueId(), "Message dialogueId cannot be null");

            // Guarantees messages for the same dialogue go to the same server.
            // Math.abs() handles potential negative hash codes.
            int index = Math.abs(message.dialogueId().hashCode()) % servers.size();
            return servers.get(index);
        }
    }

    // --- Demo Usage ---
    public static void main(String[] args) {
        List<ServerNode> nodes = List.of(
                new ServerNode("node-alpha"),
                new ServerNode("node-beta"),
                new ServerNode("node-gamma")
        );

        MessageDistributor distributor = new MessageDistributor(nodes);

        Message msg1 = new Message("dialogue-123", "Hello there!");
        Message msg2 = new Message("dialogue-abc", "What's the weather?");
        Message msg3 = new Message("dialogue-123", "How can I help you?"); // Same dialogue as msg1

        System.out.printf("Message from '%s' routed to: %s%n", msg1.dialogueId(), distributor.route(msg1).id());
        System.out.printf("Message from '%s' routed to: %s%n", msg2.dialogueId(), distributor.route(msg2).id());
        System.out.printf("Message from '%s' routed to: %s%n", msg3.dialogueId(), distributor.route(msg3).id());
    }
}
