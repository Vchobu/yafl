import yafl.Rope

final class RopeTests extends munit.FunSuite:

  test("new"):
    assertEquals(Rope().toString, "")
    assertEquals(Rope("abc").toString, "abc")

  test("isEmpty"):
    assert(Rope().isEmpty)
    assert(!Rope("abc").isEmpty)

  test("append(String)"):
    val s = Rope()
    assertEquals(s ++ "abc", Rope("abc"))
    assertEquals(s ++ "abc" ++ "def", Rope("abcdef"))
    assertEquals(s ++ "", s)

  test("append(Rope)"):
    val s = Rope()
    assertEquals(s ++ Rope("abc"), Rope("abc"))
    assertEquals(s ++ Rope("abc") ++ Rope("def"), Rope("abcdef"))
    assertEquals(s ++ (Rope("abc") ++ Rope("def")), Rope("abcdef"))
    assertEquals(s ++ Rope(), s)

  test("sameElements"):
    val lhs = Rope("abc") ++ "def"
    val rhs = Rope("ab") ++ "cd" ++ "ef"
    assert(lhs.sameElements(rhs))
    assert(!lhs.sameElements(Rope("abc")))
    assert(!lhs.sameElements(Rope("abcdefghi")))

  test("equal"):
    val lhs = Rope("abc") ++ "def"
    val rhs = Rope("ab") ++ "cd" ++ "ef"
    assertEquals(lhs, rhs)
    assertNotEquals(lhs, Rope("abc"))
    assertNotEquals(lhs, Rope("abcdefghi"))

  test("hashcode"):
    val lhs = Rope("abc") ++ "def"
    val rhs = Rope("ab") ++ "cd" ++ "ef"
    assertEquals(lhs.hashCode, rhs.hashCode)
    assertNotEquals(lhs.hashCode, Rope("abc").hashCode)
    assertNotEquals(lhs.hashCode, Rope("abcdefghi").hashCode)

  test("toString"):
    assertEquals(Rope().toString, "")
    assertEquals(Rope("").toString, "")
    assertEquals((Rope("ab") ++ "cd" ++ "ef").toString, "abcdef")

end RopeTests
