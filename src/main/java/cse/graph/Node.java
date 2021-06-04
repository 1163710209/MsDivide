package cse.graph;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 图中的节点类
 * 
 * @author 作者 E-mail:
 * @date 创建时间： 2016-3-17 下午3:04:20
 * @version 1.0
 * @parameter
 * @since
 * @return
 */
public class Node implements Comparable<Node> {
	private String id; // Node id

	// 与当前节点相邻的所有节点的ID，以及边长的
	Map<String, Double> neiborNodeId = new HashMap<>();

	public Node() {}

	public Node(String id) {
		this.id = id;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Map<String, Double> getNeiborNodeId() {
		return neiborNodeId;
	}

	public void setNeiborNodeId(Map<String, Double> neiborNodeId) {
		this.neiborNodeId = neiborNodeId;
	}

	/**
	 * 获取所有与当前节点相邻的所有节点
	 * 
	 * @return
	 */
	public Set<String> getAllNeibor() {
		// System.out.println("邻居节点有：" + neiborNodeId.keySet());
		return neiborNodeId.keySet();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return id + "--" + neiborNodeId;
	}

	@Override
	public int compareTo(Node object) {
		Node node = (Node) object;
//		if (this.id > node.getId()) {
//			return 1;
//		} else if (this.id < node.getId()) {
//			return -1;
//		}
		if (this.id.equals(object.id)) {
			return 0;
		}
		return -1;
	}
	
	public static void main(String[] args) {
		Map<String, Double> map = new HashMap<>();
		map.put("2", 3.0);
		map.put("3", 3.0);
		map.put("4", 3.0);
		map.put("5", 3.0);

		Node node1 = new Node();
		node1.setNeiborNodeId(map);
		node1.getAllNeibor();
	}

}
