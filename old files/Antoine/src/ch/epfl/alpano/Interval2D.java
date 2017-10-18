package ch.epfl.alpano;

import java.util.Objects;

/**
 * A two dimensional integer interval is the cross-product of
 * two one dimensional integer intervals
 * Example : [3..4] x [-2..0] = {(3,-2),(3,-1),(3,0),(4,-2),(4,-1),(4,0)}
 */

public final class Interval2D {
	private final Interval1D iX;
	private final Interval1D iY;

	/**
	 * @param iX one dimensional interval
	 * @param iY two dimensional interval
	 * @throws NullPointerException if one of the inputs is null
	 */
	public Interval2D(Interval1D iX, Interval1D iY) {
		if (iX == null || iY == null)
			throw new NullPointerException();
		this.iX = iX;
		this.iY = iY;
	}

	public Interval1D iX() {
		return iX;
	}

	public Interval1D iY() {
		return iY;
	}

	/**
	 * Compute if the given point is in the grid, ie
	 * if {@param x} is contained in the interval iX
	 * AND if {@param y} is contained in the interval iY
	 *
	 * @param x integer
	 * @param y integer
	 * @return true if so, false otherwise
	 */
	public boolean contains(int x, int y) {
		return (iX().contains(x) && iY().contains(y));
	}

	/**
	 * Compute the size of a two dimensional interval, ie is the size of
	 * the first interval times the size of the second one
	 *
	 * @return integer, size
	 */
	public int size() {
		return (iX().size() * iY().size());
	}

	/**
	 * Compute the size of the intersection of two bidimensional intervals
	 *
	 * @param that two dimensional interval
	 * @return integer, null if one of the two intersection is the empty set
	 */
	public int sizeOfIntersectionWith(Interval2D that) {
		return iX().sizeOfIntersectionWith(that.iX()) * iY()
				.sizeOfIntersectionWith(that.iY());
	}

	/**
	 * Compute the bounding union of two bidimensional intervals
	 *
	 * @param that interval
	 * @return two dimensional interval
	 */
	public Interval2D boundingUnion(Interval2D that) {
		return new Interval2D(iX().boundingUnion(that.iX()),
				iY().boundingUnion(that.iY()));
	}

	/**
	 * Compute if the two intervals are Unionable
	 *
	 * @param that second interval to union
	 * @return false if the intervals are disjoint
	 */
	public boolean isUnionableWith(Interval2D that) {
		return (iX().size() + iY().size() - sizeOfIntersectionWith(that)
				== boundingUnion(that).size());
	}

	/**
	 * Compute the union of the two intervals {this} and {@param that}
	 *
	 * @param that interval, not null
	 * @return interval union of intervals
	 */
	public Interval2D union(Interval2D that) {
//		Preconditions.checkArgument(isUnionableWith(that));
		return boundingUnion(that);
	}

	@Override public boolean equals(Object thatO) {
		if (this == thatO)
			return true;
		if (thatO == null || getClass() != thatO.getClass())
			return false;

		Interval2D that = (Interval2D) thatO;

		if (iX != null ? !iX.equals(that.iX) : that.iX != null)
			return false;
		return iY != null ? iY.equals(that.iY) : that.iY == null;
	}

	@Override public int hashCode() {
		return Objects.hash(iX(), iY());
	}

	@Override public String toString() {
		return iX().toString() + "x" + iY().toString();
	}
}