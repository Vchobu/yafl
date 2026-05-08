package yafl

/** The result of a notionally stateful operation.
  *
  * A result is essentially a pair composed of a value and a state that represent additional
  * information about the "context" in which the value has been computed.
  *
  * This data structure is very similar to a state monad, except that the value and state are
  * stored directly rather than meant to be computed.
  */
class Result[+T, S](val value: T, val state: S):

  /** Returns a copy of `this` with its value transformed by `transform`.
    *
    * Use this method to modify the contents of a result without modifying its state. More
    * formally, if `r = Result(v, s)`, then `r.map(f)` is equal to `Result(f(v), s)`.
    */
  def map[U](transform: T => U): Result[U, S] =
    new Result(transform(value), state)

  /** Returns the result of applying `after` on the contents of `this`.
    *
    * Use this method to combine the value of a result with a function applied in the context
    * associated with that value. That is, if `r = Result(v, s)`, then `r.and(f)` is equal to
    * `f(v)(using s)`.
    *
    * To illustrate, consider the following definitions:
    *
    *     def f(using List[Int]): Result[Int, List[Int]] =
    *       summon[List[Int] match
    *         case x :: xs => Result(x)(using xs)
    *         case _ => Result(0)(using Nil)
    *
    *     def g(n: Int)(using List[Int]): Result[Boolean, List[Int]] =
    *       Result(n == summon[List[Int]].length)
    *
    *     assert(f(using List(1, 2)).and(g).value)
    *
    * Given some context, the function `f` is assumed to produce an integer value together with
    * an updated context. We can compose this function with `g` with `and` so that `g` will be
    * applied on the integer produced by `f` and the updated context.
    */
  def and[U](after: T => (S ?=> Result[U, S])): Result[U, S] =
    after(value)(using state)

  /** Returns `value` in the context returned by `after`.
    *
    * Use this method to apply a function in the context wrapped in a result but discard its return
    * value to keep only the updated state. That is, if `r = Result(v, x)`, then `r.andDiscard(f)`
    * is equal to `Result(v)(using f(using x))`.
    */
  def andDiscard[U](after: S ?=> Result[U, S]): Result[T, S] =
    new Result(value, after(using state).state)

  /** Returns `stack` along with the result of applying `after` on the contents of `this`.
    *
    * Use this function to combine the values returned by two functions into a tuple. That is, if
    * `r = Result(a, x)` and `f(using x)` returns `Result(b, y)`, then `r.andCombine(f)` is equal
    * to `Result((a, b))(using y)`.
    */
  def andCombine[U](after: S ?=> Result[U, S]): Result[(T, U), S] =
    and((a) => after.map((b) => (a, b)))

object Result:

  /** Creates an instance wrapping `value` along with `state`. */
  def apply[T, S](value: T)(using state: S): Result[T, S] =
    new Result(value, state)

end Result
