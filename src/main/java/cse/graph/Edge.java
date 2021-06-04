package cse.graph;

/**
 * 图中表示边的类
 * 
 * @author 作者 E-mail:
 * @date 创建时间： 2016-3-17 下午2:49:49
 * @version 1.0
 * @parameter
 * @since
 * @return
 */
public class Edge {
	private String head; // 边的头节点ID
	private String tail; // 边的尾节点ID
	private double weight; // 边的权重

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public String getTail() {
		return tail;
	}

	public void setTail(String tail) {
		this.tail = tail;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	/**
	 * 边的默认构造函数
	 */
	Edge() {
		weight = 0;
	}

	Edge(String i, String j, double weight) {
		head = i;
		tail = j;
		this.weight = weight;
	}

	@Override
	public String toString() {
		return head + "--" + tail + ": " + weight;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Edge) {
			Edge edge = (Edge) obj;
			if (edge.getHead().equals(head)&&(edge.getTail().equals(tail)) ||
					(edge.getHead().equals(tail)&&(edge.getTail().equals(head)))) {
				return true;
			}
		}

		return false;
	}

	@Override
	public int hashCode() {
		return head.hashCode() + tail.hashCode();
	}
}
