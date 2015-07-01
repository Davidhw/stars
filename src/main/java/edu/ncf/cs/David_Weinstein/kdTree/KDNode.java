package edu.ncf.cs.David_Weinstein.kdTree;

import java.util.List;

import com.google.common.collect.MinMaxPriorityQueue;

public class KDNode {
  /**
   * list of doubles representing a location.
   */
  private final List<Double> location;
  /**
   * elements actually being searched through.
   */
  private final Object thingStored;
  /**
   * left child (< current).
   */
  private KDNode left;
  /**
   * right child (> current)
   */
  private KDNode right;
  /**
   * parent of current node.
   */
  private KDNode parent;
  /**
   * number of dimensions in location.
   */
  private final int dimensions;
  /**
   * on which dimension was kd tree splitting when creating this node?
   */
  private int splitDimension;

  // should maybe change this to just comparing the thing stored.
  /**
   * Check if two nodes store the same thing and have the same position.
   *
   * @param obj
   *          to be compared to.
   * @return whether or not obj and self are equal.
   */
  @Override
  public boolean equals(final Object obj) {
    final KDNode other = (KDNode) obj;
    return left == other.getLeft() && right == other.getRight()
        && parent == other.getParent() && thingStored == other.getThingStored();
  }

  /**
   * creates a KDNode by wrapping it around an object and assigning it a location.
   *
   * @param inputLocation
   *          where is the object wrapped by the KDNode?
   * @param inputThingStored
   *          object wrapped by KDNode.
   */
  public KDNode(final List<Double> inputLocation, final Object inputThingStored) {
    location = inputLocation;
    thingStored = inputThingStored;
    dimensions = inputLocation.size();
  }

  /**
   * returns number of minensions in location.
   *
   * @return
   */
  public int getDimensions() {
    return dimensions;
  }

  /**
   * returns location of thing wrapped by node.
   *
   * @return locatio.
   */
  public List<Double> getLocation() {
    return location;
  }

  /**
   * performs radius search on location.
   *
   * @param searchLocation
   *          location to search around.
   * @param radius
   *          around the search location to search within.
   * @param visited
   *          list of previously visited nodes.
   * @param neighbors
   *          queue of neighbors that gets added to as final output.
   */
  public void neighborsInRange(final List<Double> searchLocation,
      final double radius, final List<KDNode> visited,
      final MinMaxPriorityQueue neighbors) {
    visited.add(this);
    final double dist = distanceFromLocation(searchLocation);
    if (dist <= radius) {
      final NodeDistancePair ndp = new NodeDistancePair(this, dist);
      if (!neighbors.contains(ndp)) {
        neighbors.add(ndp);
      }
    }

    if (getLeft() != null
        && !visited.contains(getLeft())
        && Math.abs(getLeft().getCoordAtSplitDimension()
            - getCoordAtDimension(getLeft().splitDimension)) <= radius) {
      getLeft().neighborsInRange(searchLocation, radius, visited, neighbors);

    }

    if (getRight() != null
        && !visited.contains(getRight())
        && Math.abs(getRight().getCoordAtSplitDimension()
            - getCoordAtDimension(getRight().getSplitDimension())) <= radius) {
      getRight().neighborsInRange(searchLocation, radius, visited, neighbors);
    }

    if (getParent() != null && !visited.contains(getParent())) {
      getParent().neighborsInRange(searchLocation, radius, visited, neighbors);

    }
  }

  /**
   * finds the n nearest neighbors around a loction.
   *
   * @param searchLocation
   *          location to search around.
   * @param visited
   *          list of previously visited nodes.
   * @param nearestNeighbors
   *          queue of output that will ultimately get returned.
   * @return nearest neighbors
   */
  public MinMaxPriorityQueue<NodeDistancePair> nearestNeighborRec(
      final List<Double> searchLocation, final List<KDNode> visited,
      final MinMaxPriorityQueue<NodeDistancePair> nearestNeighbors) {
    visited.add(this);
    final NodeDistancePair ndp = new NodeDistancePair(this,
        this.distanceFromLocation(searchLocation));
    if (!nearestNeighbors.contains(ndp)) {
      nearestNeighbors.add(ndp);
    }

    if (getLeft() != null
        && !visited.contains(getLeft())
        && Math.abs(searchLocation.get(splitDimension)
            - location.get(splitDimension)) <= nearestNeighbors.peekLast()
            .getDistance()) {
      getLeft().nearestNeighborRec(searchLocation, visited, nearestNeighbors);

    }

    if (getRight() != null
        && !visited.contains(getRight())
        && Math.abs(searchLocation.get(splitDimension)
            - location.get(splitDimension)) <= nearestNeighbors.peekLast()
            .getDistance()) {
      getRight().nearestNeighborRec(searchLocation, visited, nearestNeighbors);
    }

    if (getParent() != null && !visited.contains(getParent())) {
      getParent().nearestNeighborRec(searchLocation, visited, nearestNeighbors);

    }

    return nearestNeighbors;
  }

  /**
   * returns distance from one node to another.
   *
   * @param otherNode
   *          node to measure distance from.
   * @return disatnce from other node.
   */
  public double distanceFromLocation(final KDNode otherNode) {
    return distanceFromLocation(otherNode.getLocation());
  }

  /**
   * find the distance from another location.
   *
   * @param searchLocation
   *          location to find distance from.
   * @return distance btwn node's location and another location.
   */
  public double distanceFromLocation(final List<Double> searchLocation) {
    if (location.size() != searchLocation.size()) {
      throw new IllegalArgumentException();
    } else {
      double total = 0;
      for (int dim = 0; dim < location.size(); dim++) {
        total += Math.pow((location.get(dim) - searchLocation.get(dim)), 2);
      }
      final double dist = Math.pow(total, 0.5);
      return dist;
    }
  }

  /**
   * naively goes down the KDtree to find the closest node to a location.
   *
   * @param location
   *          loc to find a node near.
   * @param currentDimension
   *          demension the tree is currently splitting on.
   * @return percieved nearest node.
   */
  public KDNode naiveSearchForClosest(final List<Double> location,
      int currentDimension) {
    if (currentDimension > location.size() - 1) {
      currentDimension = 0;
    }

    if (location.get(currentDimension) <= getCoordAtDimension(currentDimension)) {
      if (left == null) {
        return this;
      } else {
        return left.naiveSearchForClosest(location, currentDimension + 1);
      }
    } else {
      if (right == null) {
        return this;
      } else {
        return right.naiveSearchForClosest(location, currentDimension + 1);
      }
    }
  }

  /**
   * returns object wrapped by node.
   *
   * @return object wrapped by node.
   */
  public Object getThingStored() {
    return thingStored;
  }

  /**
   * sets the left child of a node.
   *
   * @param n
   *          left child.
   */
  protected void setLeft(final KDNode n) {
    left = n;
  }

  /**
   * sets the right child of a node.
   *
   * @param n
   *          right child.
   */
  protected void setRight(final KDNode n) {
    right = n;
  }

  /**
   * returns the node's location on a given axis.
   *
   * @param dimension
   *          axis.
   * @return dimension on that axis.
   */
  Double getCoordAtDimension(final int dimension) {
    return location.get(dimension);
  }

  /**
   * returns the location that the tree split on when placing this node.
   *
   * @return loc at axis tree split on.
   */
  Double getCoordAtSplitDimension() {
    return location.get(splitDimension);
  }

  /**
   * return left child.
   *
   * @return left child.
   */
  public final KDNode getLeft() {
    return left;
  }

  /**
   * return right child.
   *
   * @return right child.
   */
  public final KDNode getRight() {
    return right;
  }

  /**
   * return parent.
   *
   * @return parent.
   */
  public final KDNode getParent() {
    return parent;
  }

  /**
   * set the parent.
   *
   * @param parent
   *          parent.
   */
  public final void setParent(final KDNode parent) {
    this.parent = parent;
  }

  /**
   * get the dimension the tree splin on when placing the node.
   *
   * @return dimension tree split on.
   */
  protected final int getSplitDimension() {
    return splitDimension;
  }

  /**
   * Set the dimension the tree split on when placing the node.
   *
   * @param splitDimension
   *          dimension the tree split on.
   */
  protected final void setSplitDimension(final int splitDimension) {
    this.splitDimension = splitDimension;
  }
}
