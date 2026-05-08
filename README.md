# Yafl – Yet Another Functional Language

A toy implementation of a tiny functional programming language.

## Syntax

The syntax of Yafl is described by the production rules below.
The following is assumed:

- Integer literals are contiguous sequences of digits (e.g., `123`); and
- Identifers are strings of alphanumeric characters and the underscore, starting with a non-numeric character (e.g., `foo` or `_23`).

```
term ::=
  | unit-literal
  | boolean-literal
  | integer-literal
  | identifier
  | term-abstraction
  | term-application
  | type-abstraction
  | type-application
  | infix-application
  | conditional
  | '(' term ')'

unit-literal ::=
  | '(' ')'

boolean-literal ::=
  | 'true'
  | 'false'

term-abstraction ::=
  | '(' identifier ':' type (',' identifier ':' type)* ')' '=>' term

term-application ::=
  | term term

type-abstraction ::=
  | '[' identifier (',' identifier)* ']' '=>' term

type-application ::=
  | term '[' type (',' type)* ']'

infix-application ::=
  | term operator term

conditional ::=
  | 'if' term 'then' term 'else' term

type ::=
  | identifier
  | arrow
  | forall
  | '_'

type arrow ::=
  | type -> type

type forall ::=
  | '[' identifier (',' identifier)* ']' => type
```
