package yafl.syntax

/** An operator applied with infix notation (e.g., `+` in `1 + 2`). */
enum InfixOperator:

  case Add, Sub

object InfixOperator:

  def unapply(s: Syntax[TermTree]): Option[InfixOperator] =
    s match
      case Syntax(TermTree.Variable(n), _) => n match
        case "infix+" => Some(Add)
        case "infix-" => Some(Sub)
        case _ => None
      case _ => None

end InfixOperator
