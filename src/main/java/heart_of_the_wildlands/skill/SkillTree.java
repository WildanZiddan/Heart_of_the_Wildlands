package heart_of_the_wildlands.skill;

import java.util.*;

public class SkillTree {
    private SkillNode root;
    private HashSet unlockedSkills = new HashSet();

    public void insertSkill(String name, int level) {
        SkillNode newSkill = new SkillNode(name, level);
        root = insertSkill(root, newSkill);
    }

    private SkillNode insertSkill(SkillNode root, SkillNode newSkill) {
        if (root == null){
           return newSkill;
        }
        if (newSkill.getRequiredLevel() < root.getRequiredLevel()) {
            root.setLeft(insertSkill(root.getLeft(), newSkill));
        }
        else {
            root.setRight(insertSkill(root.getRight(), newSkill));
        }
        return root;
    };

    public SkillTree() {
        createSkills();
    }

    private void createSkills() {
        insertSkill("Slash", 1);
        insertSkill("Heavy Slash", 2);
        insertSkill("Quick Slash", 2);
        insertSkill("Mega Slash", 3);
        insertSkill("Frenzy Slash", 4);

        unlockedSkills.add("Slash"); // ✅ Unlock root skill by default
    }


    public ArrayList getAvailableSkills(int level) {
        ArrayList newSkills = new ArrayList();
        findNewSkills(root, level, newSkills);
        return newSkills;
    }

    private void findNewSkills(SkillNode node, int level, ArrayList list) {
        if (node == null) return;

        if (level >= node.getRequiredLevel() && !unlockedSkills.contains(node.getName())) {
            list.add(node.getName());
            unlockedSkills.add(node.getName());
        }

        findNewSkills(node.getLeft(), level, list);
        findNewSkills(node.getRight(), level, list);
    }

    public ArrayList getAllUnlockedSkills() {
        ArrayList orderedSkills = new ArrayList();
        Queue<SkillNode> queue = new LinkedList<>();
        if (root != null) queue.add(root);

        while (!queue.isEmpty()) {
            SkillNode node = queue.poll();
            if (unlockedSkills.contains(node.getName())) {
                orderedSkills.add(node.getName());
            }

            if (node.getLeft() != null) queue.add(node.getLeft());
            if (node.getRight() != null) queue.add(node.getRight());
        }

        return orderedSkills;
    }

}
