package edu.ncf.cs.David_Weinstein.WeinsteinStars;

/**
 * Represents stars - their id, name, and location.
 *
 * @author david weinstein
 *
 */
public class Star {
  /**
   * The star's id.
   */
  private int starID;
  /**
   * The star's name in English if it has one.
   */
  private String properName;
  /**
   * The star's x coordinate.
   */
  private double x;
  /**
   * The star's y coordinate.
   */
  private double y;
  /**
   * The star's z coordinate.
   */
  private double z;

  /**
   *
   * @param inputId
   *          The id of the star.
   * @param inputName
   *          The English name of the star if it has one.
   * @param inputX
   *          The x coordinate of the stars location.
   * @param inputY
   *          The y coordinate of the stars location.
   * @param inputZ
   *          The z coordinate of the stars location.
   */
  public Star(final int inputId, final String inputName, final double inputX,
      final double inputY, final double inputZ) {
    setStarID(inputId);
    setProperName(inputName);
    setX(inputX);
    setY(inputY);
    setZ(inputZ);
  }

  @Override
  /**
   * Returns a string representation of the star.
   */
  public final String toString() {
    return "" + starID + ", " + properName + " at " + x + ", " + y + ", " + z;
  }

  /**
   * Returns the star's id.
   *
   * @return star's id.
   */
  protected final int getStarID() {
    return starID;
  }

  /**
   * Sets the star's id.
   *
   * @param inputID
   *          the id to give the star.
   */
  protected final void setStarID(final int inputID) {
    starID = inputID;
  }

  /**
   * Returns the English name of the star.
   *
   * @return English name of the star.
   */
  protected final String getProperName() {
    return properName;
  }

  /**
   * Sets the English name of the star.
   *
   * @param inputName
   *          English name of the star.
   */
  protected final void setProperName(final String inputName) {
    properName = inputName;
  }

  /**
   * Returns the x coordinate of the star's location.
   *
   * @return the star's location's x coordinate.
   */
  protected final double getX() {
    return x;
  }

  /**
   * Set the x coordinate of the star.
   *
   * @param inputX
   *          The x coordinate of the star.
   */
  protected final void setX(final double inputX) {
    x = inputX;
  }

  /**
   * Returns the y coordinate of the star's location.
   *
   * @return the star's location's y coordinate.
   */
  protected final double getY() {
    return y;
  }

  /**
   * Set the y coordinate of the star.
   *
   * @param inputY
   *          The y coordinate of the star.
   */
  protected final void setY(final double inputY) {
    y = inputY;
  }

  /**
   * Returns the z coordinate of the star's location.
   *
   * @return the star's location's z coordinate.
   */
  protected final double getZ() {
    return z;
  }

  /**
   * Set the z coordinate of the star.
   *
   * @param inputZ
   *          The x coordinate of the star.
   */
  protected final void setZ(final double inputZ) {
    z = inputZ;
  }

}
