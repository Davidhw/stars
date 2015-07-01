package edu.ncf.cs.David_Weinstein.kdTree;

/**
 * Class for coupling nodes with a distance from some point.
 *
 * @author david weinstein
 */
public class NodeDistancePair implements Comparable {
  /**
   * node being wrapped.
   */
  private KDNode node;
  /**
   * distance between node and some location.
   */
  private Double distance;

  /**
   * Creates a object storing a node and its distance from some point.
   *
   * @param inputNode
   *          node to wrap.
   * @param inputDistance
   *          distance to wrap.
   */
  NodeDistancePair(final KDNode inputNode, final double inputDistance) {
    node = inputNode;
    distance = inputDistance;
  }

  /**
   * get the wrapped node.
   *
   * @return wrapped node.
   */
  protected final KDNode getNode() {
    return node;
  }

  /**
   * set what node is wrapped.
   *
   * @param node
   *          node to wrap.
   */
  protected final void setNode(final KDNode node) {
    this.node = node;
  }

  /**
   * Get the distance between the node and a point.
   *
   * @return distance.
   */
  protected final double getDistance() {
    return distance;
  }

  /**
   * Set the distance between the node and some point.
   *
   * @param distance
   *          between node and point.
   */
  protected final void setDistance(final double distance) {
    this.distance = distance;
  }

  /**
   * Make NDPs directly comparable by comparing their distacnes.
   *
   * @param o
   *          other NDP
   * @return distance compared with other distance.
   */
  @Override
  public int compareTo(final Object o) {
    final NodeDistancePair other = (NodeDistancePair) o;
    return distance.compareTo(other.getDistance());
  }

  /**
   * make the hashcode the node's hashcode.
   *
   * @return node's hashcode.
   */
  @Override
  public int hashCode() {
    return node.hashCode();
  }

  /**
   * check for equality by comparing hashcodes.
   *
   * @param o
   *          other NodeDistancePair
   * @return are they equal?
   */
  @Override
  public boolean equals(final Object o) {
    return ((NodeDistancePair) o).hashCode() == hashCode();
  }

}
