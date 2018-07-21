* Added support for defines. Use `#define A B` to create or replace a define, use `#undef A` to remove a define. Defined names can be used where targets or number literals are expected.
* Added two new instructions, `RRLAST` and `RLLAST`, used to rotate the value of `LAST` one to the right or the left, respectively, if its value is not `NIL`.
* Added a new module, the sequencer module.
* Updated Charset Wires integration (asiekierka).
* Fix crash when controllers in errored state are in view and player is looking at an Entity (as opposed to a block).
* Fix crash when rendering module overlay when player is looking at an Entity (as opposed to a block).
* Fix modules potentially stopping in combination with the serial port module (and potentially modded modules).