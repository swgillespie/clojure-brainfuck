## clojure-brainfuck

A small implementation of brainfuck in Clojure. All data structures including
the memory tape itself are immutable, which is pretty cool. The memory tape is infinite
thanks to the power of laziness.

I was a little disappointed in the JVM's lack of tail-call optimization but Clojure's
`recur` and `trampoline` forms definitely make up for it. This implementation makes
heavy use of tail-calls. Instead of actually performing the tail call, as I might do in
Scheme, we simply return the function that we would be calling and trampoline will do the
rest. recur does something similar except for direct recursion.

### Building and running
This project uses leinigen. To run,
```
git clone https://github.com/swgillespie/clojure-brainfuck.git
cd clojure-brainfuck
lein uberjar
echo "++++++++[>++++[>++>+++>+++>+<<<<-]>+>+>->>+[<]<-]>>.>---.+++++++..+++.>>.<-.<.+++.------.--------.>>+.>++." | java -jar target/brainfuck-0.0.1-standalone.jar
```

You can also play with the internal functions by starting a repl with `lein repl`.
The main entry function with the trampoline is `brainfuck.core/execute`.