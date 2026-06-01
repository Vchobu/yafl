import com.dylibso.chicory
import com.dylibso.chicory.tools.wasm.Wat2Wasm

import java.io.File

import yafl.SourceFile
import yafl.emitter.Emitter
import yafl.optimizer.Optimizer
import yafl.parser.Parser
import yafl.typer.Typer

final class EmitterTests extends munit.FunSuite:

  test("argc"):
    val input = SourceFile("test", "#argc")
    val wasm = compile(input)
    val main = wasm.`export`("main")
    writeArguments(wasm, IArray(31, 11))
    assertEquals(main.apply()(0), 2L)

  test("argv"):
    val input = SourceFile("test", "(#argv 0) + (#argv 1)")
    val wasm = compile(input)
    val main = wasm.`export`("main")
    writeArguments(wasm, IArray(40, 2))
    assertEquals(main.apply()(0), 42L)

  test("integer addition"):
    val input = SourceFile("test", "40 + 2")
    val main = compile(input).`export`("main")
    assertEquals(main.apply()(0), 42L)

  test("conditional_1"):
    val input = SourceFile("test", "if true then 1 else 2")
    val main = compile(input).`export`("main")
    assertEquals(main.apply()(0), 1L)

  test("conditional_2"):
    val input = SourceFile("test", "if 1 < 2 then if 2 < 3 then 7 else 8 else 9")
    val main = compile(input).`export`("main")
    assertEquals(main.apply()(0), 7L)

  test("conditional_3"):
    val input = SourceFile("test", "if true then let x = 10 ; x + 1 else let x = 20 ; x + 2")
    val main = compile(input).`export`("main")
    assertEquals(main.apply()(0), 11L)

  test("conditional_4"):
    val input = SourceFile("test", "if 3 * 2 == 6 then 100 else 0")
    val main = compile(input).`export`("main")
    assertEquals(main.apply()(0), 100L)

  test("binding_1"):
    val input = SourceFile("test", "let x = 1 + 2; x + 3")
    val main = compile(input).`export`("main")
    assertEquals(main.apply()(0), 6L)

  test("binding_2"):
    val input = SourceFile("test", "let x = 2; x + x")
    val main = compile(input).`export`("main")
    assertEquals(main.apply()(0), 4L)

  test("binding_3"):
    val input = SourceFile("test", "let x = 1 + 2; x * 3")
    val main = compile(input).`export`("main")
    assertEquals(main.apply()(0), 9L)

  test("binding_4"):
    val input = SourceFile("test", "let x = 1 ; if x < 2 then let x = 2 ; x else x")
    val main = compile(input).`export`("main")
    assertEquals(main.apply()(0), 2L)

  test("binding_5"):
    val input = SourceFile("test", "let x = 1 ; let y = let x = 2 ; x + 3 ; x + y")
    val main = compile(input).`export`("main")
    assertEquals(main.apply()(0), 6L)

  test("binding_6"):
    val input = SourceFile("test", "let a = 5 ; let b = a * 2 ; b + a")
    val main = compile(input).`export`("main")
    assertEquals(main.apply()(0), 15L)

  test("binding_7"):
    val input = SourceFile("test", "let x = 1 ; (let x = 2 ; x) + x")
    val main = compile(input).`export`("main")
    assertEquals(main.apply()(0), 3L)

  test("binding_8"):
    val input = SourceFile("test", "let x = 1 ; let y = (let x = 2 ; x) ; x")
    val main = compile(input).`export`("main")
    assertEquals(main.apply()(0), 1L)

  test("binding_9"):
    val input = SourceFile("test", "let x = 1 ; let x = 1 ; let x = 2 ; let x = 3 ; x")
    val main = compile(input).`export`("main")
    assertEquals(main.apply()(0), 3L)

  /** Compiles `input` to a WebAssembly module and returns an instance of it. */
  private def compile(input: SourceFile): chicory.runtime.Instance =
    val program =  Optimizer.optimize(Typer.check(Parser.parse(input)))
    val binary = Wat2Wasm.parse(Emitter.emit(program))
    val m = chicory.wasm.Parser.parse(binary)
    chicory.runtime.Instance.builder(m).build()

  /** Initializes the command-line arguments of `wasm` to `values`. */
  private def writeArguments(wasm: chicory.runtime.Instance, values: IArray[Int]): Unit =
    val m = wasm.memory()
    m.writeI32(0, values.length)
    for i <- 0 until values.length do m.writeI32(4 + (i * 4), values(i))

end EmitterTests
