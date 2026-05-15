/** 
 * Abstract class for all model plan
 * @author (Shrawan karki)
 */
import java.io.Serializable;

public abstract class AIModel implements Serializable {

    private String name;
    private double tokenPrice;
    private int parameters;
    private int maxContext;

    public AIModel(String name, double tokenPrice,
                   int parameters, int maxContext) {

        this.name = name;
        this.tokenPrice = tokenPrice;
        this.parameters = parameters;
        this.maxContext = maxContext;
    }

    //getter methods
    public String getName() {
        return name;
    }

    public double getTokenPrice() {
        return tokenPrice;
    }

    public int getParameters() {
        return parameters;
    }

    public int getMaxContext() {
        return maxContext;
    }

    // token estimation logic
    private int estimateTokens(String text) {
        return text.length() / 4;
    }

    // token usage calculation
    public boolean calculateTokenUsage(String text, int outputTokens) {

        int inputTokens = estimateTokens(text);
        int totalTokens = inputTokens + 50 + outputTokens;

        return totalTokens <= maxContext;
    }

    // report of token
    public String getTokenReport(String text, int outputTokens) {

        int inputTokens = estimateTokens(text);
        int totalTokens = inputTokens + 50 + outputTokens;

        String report = "===== Token Usage Report =====\n";
        report += "Input Tokens: " + inputTokens + "\n";
        report += "Output Tokens: " + outputTokens + "\n";
        report += "System Overhead: 50\n";
        report += "Total Tokens: " + totalTokens + "\n";
        report += "Max Context: " + maxContext + "\n";

        if (totalTokens <= maxContext) {
            report += "Status: WITHIN LIMIT ";
        } else {
            report += "Status: EXCEEDS LIMIT ";
        }

        return report;
    }

    // abstract method
    public abstract String processPrompt(String text, int tokens);

    // display details
    public String showDetails() {

        return "AI Model: " + name +
               "\nToken Price (per 100K): NPR " + tokenPrice +
               "\nParameters (Billion): " + parameters +
               "\nMax Context: " + maxContext + " tokens";
    }
}