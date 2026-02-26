package heart_of_the_wildlands.skill;

public class SkillNode {
    private String name;
    private int requiredLevel;
    private SkillNode left;  // could be considered as a weaker path
    private SkillNode right; // stronger or alternate skill path

    public SkillNode(String name, int requiredLevel) {
        this.name = name;
        this.requiredLevel = requiredLevel;
        this.left = null;
        this.right = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRequiredLevel() {
        return requiredLevel;
    }

    public void setRequiredLevel(int requiredLevel) {
        this.requiredLevel = requiredLevel;
    }

    public SkillNode getLeft() {
        return left;
    }

    public void setLeft(SkillNode left) {
        this.left = left;
    }

    public SkillNode getRight() {
        return right;
    }

    public void setRight(SkillNode right) {
        this.right = right;
    }
}
