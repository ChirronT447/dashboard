package com.gateway.dashboard.interviews.ctdl;

/**
 * A stateful parser that processes a stream of data arriving in chunks.
 * It buffers incoming data and prints complete messages as they are formed.
 */
public class MessageStreamParser {

    private final StringBuilder buffer = new StringBuilder();

    /**
     * Processes a chunk of an incoming data stream. It appends the chunk
     * to an internal buffer and parses and prints as many complete messages
     * as possible.
     *
     * @param chunk A piece of the input string.
     */
    public void processChunk(String chunk) {
        if (chunk == null || chunk.isEmpty()) {
            return;
        }

        // 1. Append the new chunk to our internal buffer.
        buffer.append(chunk);

        // 2. Continuously try to process complete messages from the buffer.
        while (true) {
            // We need at least 2 chars to read the length. If not, wait for more data.
            if (buffer.length() < 2) {
                break;
            }

            // Read and parse the length of the next message.
            String lengthStr = buffer.substring(0, 2);
            int messageLength = Integer.parseInt(lengthStr);

            // Check if the full message block (length prefix + message) is in the buffer.
            int totalBlockLength = 2 + messageLength;
            if (buffer.length() < totalBlockLength) {
                // Message is incomplete, wait for the next chunk.
                break;
            }

            // If we are here, we have a complete message.
            // 3. Extract and print the message.
            String message = buffer.substring(2, totalBlockLength);
            System.out.print(message);

            // 4. CRITICAL: Remove the processed message block from the buffer.
            buffer.delete(0, totalBlockLength);
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Part 2: Streaming Demo ---");
        System.out.println("Example: PrintMessage(\"02bc1012345\") followed by PrintMessage(\"67890\")");

        // Create a single parser instance to maintain state across calls.
        MessageStreamParser parser = new MessageStreamParser();

        System.out.print("Output: ");
        parser.processChunk("02bc1012345");
        parser.processChunk("67890");
        System.out.println(); // Add a newline for clean output.

        System.out.println("\n--- Another Streaming Example ---");
        MessageStreamParser parser2 = new MessageStreamParser();
        System.out.print("Output: ");
        parser2.processChunk("0"); // Incomplete length
        parser2.processChunk("5hello"); // Complete length, complete message
        parser2.processChunk("06world"); // Complete message
       // parser2.processChunk("04... and more"); // Incomplete message
       // parser2.processChunk(" data"); // Complete message
        System.out.println();
    }
}
