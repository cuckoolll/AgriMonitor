package com.agri.monitor.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.agri.monitor.vo.Node;

public class TreeBuilder {

	private TreeBuilder() {}
	
    @SuppressWarnings("unchecked")
    public static List<Node> buildListToTree(List<Node> dirs) {
        List<Node> roots = findRoots(dirs);
        List<Node> notRoots = (List<Node>) CollectionUtils
                .subtract(dirs, roots);
        for (Node root : roots) {
            root.setChildren(findChildren(root, notRoots));
        }
        return roots;
    }

    private static List<Node> findRoots(List<Node> allNodes) {
        List<Node> results = new ArrayList<Node>();
        for (Node node : allNodes) {
            boolean isRoot = true;
            for (Node comparedOne : allNodes) {
                if (node.getParentId() == comparedOne.getId()) {
                    isRoot = false;
                    break;
                }
            }
            if (isRoot) {
                node.setLevel(0);
                results.add(node);
                node.setRootId(node.getId());
            }
        }
        return results;
    }

    @SuppressWarnings("unchecked")
    private static List<Node> findChildren(Node root, List<Node> allNodes) {
        List<Node> children = new ArrayList<Node>();

        for (Node comparedOne : allNodes) {
            if (comparedOne.getParentId() == root.getId()) {
                comparedOne.setParent(root);
                comparedOne.setLevel(root.getLevel() + 1);
                children.add(comparedOne);
            }
        }
        List<Node> notChildren = (List<Node>) CollectionUtils.subtract(allNodes, children);
        for (Node child : children) {
            List<Node> tmpChildren = findChildren(child, notChildren);
            if (tmpChildren == null || tmpChildren.size() < 1) {
                child.setLeaf(true);
            } else {
                child.setLeaf(false);
            }
            child.setChildren(tmpChildren);
        }
        return children;
    }

    public static void main(String[] args) {
        TreeBuilder tb = new TreeBuilder();
        List<Node> allNodes = new ArrayList<Node>();
        allNodes.add(new Node(28, 0, "节点1"));
        allNodes.add(new Node(29, 0, "节点2"));
        allNodes.add(new Node(30, 0, "节点3"));
        allNodes.add(new Node(31, 28, "节点4"));
        allNodes.add(new Node(32, 28, "节点5"));
        allNodes.add(new Node(33, 28, "节点6"));
        List<Node> roots = tb.buildListToTree(allNodes);
        for (Node n : roots) {
            System.out.println(n);
        }

    }
}