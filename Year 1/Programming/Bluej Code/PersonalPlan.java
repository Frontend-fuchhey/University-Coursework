/**
 * Personal plan model child class of AIModel
 */
public class PersonalPlan extends AIModel {

    private int monthlyLimit;

    public PersonalPlan(String name, double tokenPrice,
                        int parameters, int maxContext,
                        int monthlyLimit) {

        super(name, tokenPrice, parameters, maxContext);
        this.monthlyLimit = monthlyLimit;
    }

    // checking remaining quota
    public int remainingQuota() {
        return monthlyLimit;
    }

    // adding extra qquota
    public String addMorePrompts(int extra) {

        if (extra <= 0) {
            return "Invalid input. Prompts must be greater than zero.";
        }

        monthlyLimit += extra;
        return "Quota updated. Current prompts: " + monthlyLimit;
    }

    @Override
    public String processPrompt(String text, int tokens) {

        if (tokens > getMaxContext()) {
            return "Request denied: exceeds context size.";
        }

        if (monthlyLimit <= 0) {
            return "No prompts left for this month.";
        }

        monthlyLimit--;

        return "Processing Prompt...\n" +
               "Text: " + text + "\n" +
               "Tokens Used: " + tokens + "\n" +
               "Remaining Quota: " + monthlyLimit;
    }

    @Override
    public String showDetails() {
        return super.showDetails() +
               "\nMonthly Prompts Remaining: " + monthlyLimit;
    }
}