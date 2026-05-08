package yafl

/** A character string supporting fast concatenation. */
final class Rope private (private val contents: Vector[String]) extends AnyVal:

  /** Returns `this` concatenated with `that`. */
  def ++(that: String): Rope =
    if that.isEmpty then this else new Rope(contents :+ that)

  /** Returns `this` concatenated with `that`. */
  def ++(that: Rope): Rope =
    new Rope(this.contents ++ that.contents)

  /** Returns `true` iff `this` is empty. */
  def isEmpty: Boolean =
    contents.isEmpty

  /** Returns `true` iff `this` is equal to `that`. */
  def sameElements(that: Rope): Boolean =
    val (lhs, rhs) = (this.contents, that.contents)
    def loop(i: Int, x: Int, j: Int, y: Int): Boolean =
      if i < lhs.length then
        if x < lhs(i).length then
          if j < rhs.length then
            if y < rhs(j).length then
              (lhs(i)(x) == rhs(j)(y)) && loop(i, x + 1, j, y + 1)
            else
              loop(i, x, j + 1, 0)
          else
            false
        else
          loop(i + 1, 0, j, y)
      else
        (j >= rhs.length) || (y == rhs(j).length && loop(i, x, j + 1, 0))
    loop(0, 0, 0, 0)

  override def equals(that: Any): Boolean =
    that match
      case other: Rope => this.sameElements(other)
      case _ => false

  override def hashCode(): Int =
    contents.foldLeft(13) { (h, s) =>
      s.foldLeft(h)((i, c) => (i << 16) ^ i | c)
    }

  override def toString(): String =
    contents.mkString

object Rope:

  /** Creates an instance with the given value. */
  def apply(value: String = ""): Rope =
    new Rope(if value.isEmpty then Vector() else Vector(value))

end Rope
