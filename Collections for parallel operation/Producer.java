public class Producer implements Runnable {
    private final int textsToGenerate;
    private final int textLength;

    public Producer(int textsToGenerate, int textLength) {
        this.textsToGenerate = textsToGenerate;
        this.textLength = textLength;
    }

    private String generateText(String letters, int length) {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt((int) (Math.random() * letters.length())));
        }
        return text.toString();
    }

    @Override
    public void run() {
        for (int i = 0; i < textsToGenerate; i++) {
            String text = generateText("abc", textLength);
            try {
                Main.queueA.put(text);
                Main.queueB.put(text);
                Main.queueC.put(text);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
