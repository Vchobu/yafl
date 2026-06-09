
import yafl.SourceFile
import yafl.optimizer.Optimizer
import yafl.parser.Parser
import yafl.syntax.{Syntax, TermTree}
import yafl.typer.{TypedProgram, Typer}

final class OptimizerTests extends munit.FunSuite:

  test("constant folding"):
    val optimized = optimize("1 + 2 + 3")
    (optimized.syntax.value : @unchecked) match
      case TermTree.IntegerLiteral(6) => ()

  test("multiplication"):
    val optimized = optimize("3 * 3")
    (optimized.syntax.value : @unchecked) match
      case TermTree.IntegerLiteral(9) => ()

  test("division"):
    val optimized = optimize("66 / 3")
    (optimized.syntax.value : @unchecked) match
      case TermTree.IntegerLiteral(22) => ()

  test("equality"):
    val optimized = optimize("3 == 3")
    (optimized.syntax.value : @unchecked) match
      case TermTree.BooleanLiteral(true) => ()
  
  test("equality 2"):
    val optimized = optimize("3 == 4")
    (optimized.syntax.value : @unchecked) match
      case TermTree.BooleanLiteral(false) => ()

  test("inequality"):
    val optimized = optimize("3 != 4")
    (optimized.syntax.value : @unchecked) match
      case TermTree.BooleanLiteral(true) => ()
    
  test("inequality 2"):
    val optimized = optimize("3 != 3")
    (optimized.syntax.value : @unchecked) match
      case TermTree.BooleanLiteral(false) => ()

  test("less than"):
    val optimized = optimize("3 < 4")
    (optimized.syntax.value : @unchecked) match
      case TermTree.BooleanLiteral(true) => ()

  test("less than or equal"):
    val optimized = optimize("3 <= 4")
    (optimized.syntax.value : @unchecked) match
      case TermTree.BooleanLiteral(true) => ()

  test("less than 2"):
    val optimized = optimize("3 < 3")
    (optimized.syntax.value : @unchecked) match
      case TermTree.BooleanLiteral(false) => ()

  test("less than or equal 2"):
    val optimized = optimize("3 <= 3")
    (optimized.syntax.value : @unchecked) match
      case TermTree.BooleanLiteral(true) => ()

  test("less than or equal 2"):
    val optimized = optimize("3 <= 2")
    (optimized.syntax.value : @unchecked) match
      case TermTree.BooleanLiteral(false) => ()

  test("normalization"):
    import TermTree.TermApplication as F
    import TermTree.Binding as B
    val optimized = optimize("let x = 0; x + 1")
    (optimized.syntax.value : @unchecked) match
      case B(_, _, Syntax(F(lhs, Syntax(TermTree.Variable("x"), _)), _)) =>
        (lhs.value : @unchecked) match
          case F(_, Syntax(TermTree.IntegerLiteral(1), _)) => ()

  /** Compiles `input` to a WebAssembly module and returns an instance of it. */
  private def optimize(input: String): TypedProgram =
    Optimizer.optimize(Typer.check(Parser.parse(SourceFile("test", input))))

end OptimizerTests
