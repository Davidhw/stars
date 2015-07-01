package edu.ncf.cs.David_Weinstein.kdTree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.MinMaxPriorityQueue;

public class KDTree {
  private final int dimensions;
  KDNode root;

  /**
   * Get which dimension to split on next.
   *
   * @param lastDimensionSplitOn
   *          which dimension was previously split upon.
   * @return Dimension to split on.
   */
  private final int getNextDimensionToSplitOn(final int lastDimensionSplitOn) {
    return (lastDimensionSplitOn + 1) % dimensions;
  }

  /**
   * Finds the index median values of the nodes along the specified dimension.
   *
   * @param nodes
   *          List of nodes not yet added to the tree.
   * @param dimension
   *          Dimension being split upon.
   * @return value to split dimension upon.
   */
  private final int findSplitIndex(final List<KDNode> nodes, final int dimension) {
    if (nodes.size() == 1) {
      return 0;
    }

    final List<Double> coordsAtDimension = new ArrayList<Double>();
    final List<Double> unsortedCoordsAtDimension = new ArrayList<Double>();

    for (final KDNode node : nodes) {
      coordsAtDimension.add(node.getCoordAtDimension(dimension));
      unsortedCoordsAtDimension.add(node.getCoordAtDimension(dimension));
    }
    coordsAtDimension.sort(null);
    final int middleIndex = Math.floorDiv(coordsAtDimension.size(), 2) - 1;
    return unsortedCoordsAtDimension
        .indexOf(coordsAtDimension.get(middleIndex));
  }

  /**
   * Deprecated function to add n nearest neighbors to a queue.
   *
   * @param neighbors
   *          nearest neighbors already identified.
   * @param numNeighbors
   *          number of neighbors to add.
   * @param location
   *          location neighbors should be near.
   */
  public void addNeighbors(
      final MinMaxPriorityQueue<NodeDistancePair> neighbors,
      final int numNeighbors, final List<Double> location) {
    if (numNeighbors < 1) {
      return;
    }

    final LinkedList<KDNode> children = new LinkedList<KDNode>();
    children.add(root);
    neighbors.add(new NodeDistancePair(root, root
        .distanceFromLocation(location)));
    int added = 1;
    while (!children.isEmpty() && added < numNeighbors) {
      final KDNode current = children.poll();
      if (current.getLeft() != null) {
        children.add(current.getLeft());
      }

      if (current.getRight() != null) {
        children.add(current.getRight());
      }

      if (!neighbors.contains(new NodeDistancePair(current, current
          .distanceFromLocation(location)))) {
        neighbors.add(new NodeDistancePair(current, current
            .distanceFromLocation(location)));
        added += 1;
      }
    }
  }

  /**
   * Find neighbors within some distance of a location.
   *
   * @param location
   *          loc to search around.
   * @param radius
   *          radius within which neighbors should be of location.
   * @return neighbors within radius of location.
   */
  public List<KDNode> neighborsInRange(final List<Double> location,
      final double radius) {
    final KDNode currentBest = root.naiveSearchForClosest(location, 0);
    final MinMaxPriorityQueue<NodeDistancePair> neighbors = MinMaxPriorityQueue
        .create();
    final List<KDNode> visited = new ArrayList<KDNode>();
    currentBest.neighborsInRange(location, radius, visited, neighbors);

    final List<KDNode> output = new ArrayList<KDNode>();

    while (!neighbors.isEmpty()) {
      output.add(neighbors.pollFirst().getNode());
    }
    return output;
  }

  /**
   * Find nearest neighbors around some location
   *
   * @param location
   *          loc to search around.
   * @param numNeighbors
   *          number of neighbors to return.
   * @return nearest neighbors.
   */
  public List<KDNode> nearestNeighbors(final List<Double> location,
      final int numNeighbors) {
    MinMaxPriorityQueue<NodeDistancePair> nearestNeighbors = MinMaxPriorityQueue
        .maximumSize(numNeighbors).create();

    // addNeighbors(nearestNeighbors, numNeighbors, location);
    final KDNode currentBest = root.naiveSearchForClosest(location, 0);
    final List<KDNode> visited = new ArrayList<KDNode>();

    nearestNeighbors = currentBest.nearestNeighborRec(location, visited,
        nearestNeighbors);

    final List<KDNode> output = new ArrayList<KDNode>();

    while (!nearestNeighbors.isEmpty()) {
      output.add(nearestNeighbors.pollFirst().getNode());
    }

    return output;
  }

  /**
   * Create a KD Tree.
   *
   * @param nodes
   *          nodes to create the tree from.
   * @param inputDimensions
   *          number of coordinates in each location.
   */
  public KDTree(final List<KDNode> nodes, final int inputDimensions) {
    dimensions = inputDimensions;
    root = createNode(nodes, 0, null);
  }

  /**
   * Recursively create KDNodes
   *
   * @param nodes
   *          list of nodes left to be added to tree.
   * @param dimension
   *          current dimension being split on.
   * @param parent
   *          parent of current node.
   * @return a KDNode.
   */
  private KDNode createNode(final List<KDNode> nodes, final int dimension,
      final KDNode parent) {
    if (nodes.size() == 0) {
      return null;
    }

    final int splitIndex = findSplitIndex(nodes, dimension);
    final KDNode retNode = nodes.get(splitIndex);
    retNode.setSplitDimension(dimension);
    retNode.setParent(parent);
    nodes.remove(splitIndex);
    final double splitVal = retNode.getCoordAtDimension(dimension);

    retNode.setLeft(createNode(
        nodes.stream()
        .filter(n -> n.getCoordAtDimension(dimension) <= splitVal)
        .collect(Collectors.toList()),
        getNextDimensionToSplitOn(dimension), retNode));

    retNode.setRight(createNode(
        nodes.stream().filter(n -> n.getCoordAtDimension(dimension) > splitVal)
        .collect(Collectors.toList()),
        getNextDimensionToSplitOn(dimension), retNode));

    return retNode;
  }

  /**
   * return the trees root.
   *
   * @return root.
   */
  public KDNode getRoot() {
    return root;
  }
}
