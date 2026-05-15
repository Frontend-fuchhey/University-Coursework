/**
 * pro plan model Child class of AIModel
 */
public class ProPlan extends AIModel {

    private int teamCapacity;

    public ProPlan(String name, double tokenPrice,
                   int parameters, int maxContext,
                   int teamCapacity) {

        super(name, tokenPrice, parameters, maxContext);
        this.teamCapacity = teamCapacity;
    }

    //  checking available slots
    public int slotsLeft() {
        return teamCapacity;
    }

    // add teeam member
    public String includeMember(String member) {

        if (member == null || member.trim().isEmpty()) {
            return "Invalid member name.";
        }

        if (teamCapacity <= 0) {
            return "Team is full. Cannot add more members.";
        }

        teamCapacity--;
        return member + " successfully joined the team.";
    }

    // remove member
    public String excludeMember(String member) {

        if (member == null || member.trim().isEmpty()) {
            return "Invalid member name.";
        }

        teamCapacity++;
        return member + " removed from team.";
    }

    @Override
    public String processPrompt(String text, int tokens) {

        if (tokens > getMaxContext()) {
            return "Token request exceeds model capacity.";
        }

        return "Pro Model Response Generated\n" +
               "Input: " + text + "\n" +
               "Token Count: " + tokens + "\n" +
               "(No monthly restriction)";
    }

    @Override
    public String showDetails() {
        return super.showDetails() +
               "\nTeam Slots Available: " + teamCapacity;
    }
}