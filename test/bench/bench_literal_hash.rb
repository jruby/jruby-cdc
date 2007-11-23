require 'benchmark'

TIMES = (ARGV[0] || 5).to_i
Benchmark.bm(30) {|bm|
  TIMES.times {
    bm.report("loop alone") {
      a = 1; while a < 100_000; a += 1; end
    }
  }

  TIMES.times {
    bm.report("no-element hash") {
      a = 1; while a < 100_000; {};{};{};{};{};{};{};{};{};{}; a += 1; end
    }
  }

  TIMES.times {
    bm.report("1-element hash") {
      x = 1
      a = 1; while a < 100_000; {x=>x}; {x=>x}; {x=>x}; {x=>x}; {x=>x}; {x=>x}; {x=>x}; {x=>x}; {x=>x}; {x=>x}; a += 1; end
    }
  }

  TIMES.times {
    bm.report("2-element hash") {
      x = 1
      y = 2
      a = 1; while a < 100_000; {x=>x,y=>y};{x=>x,y=>y};{x=>x,y=>y};{x=>x,y=>y};{x=>x,y=>y};{x=>x,y=>y};{x=>x,y=>y};{x=>x,y=>y};{x=>x,y=>y};{x=>x,y=>y}; a += 1; end
    }
  }

  TIMES.times {
    bm.report("3-element hash") {
      x = 1
      y = 2
      z = 3
      a = 1; while a < 100_000; {x=>x,y=>y,z=>z};{x=>x,y=>y,z=>z};{x=>x,y=>y,z=>z};{x=>x,y=>y,z=>z};{x=>x,y=>y,z=>z};{x=>x,y=>y,z=>z};{x=>x,y=>y,z=>z};{x=>x,y=>y,z=>z};{x=>x,y=>y,z=>z};{x=>x,y=>y,z=>z}; a += 1; end
    }
  }

  TIMES.times {
    bm.report("4-element hash") {
      x = 1
      y = 2
      z = 3
      w = 4
      a = 1; while a < 100_000; {x=>x,y=>y,z=>z,w=>w};{x=>x,y=>y,z=>z,w=>w};{x=>x,y=>y,z=>z,w=>w};{x=>x,y=>y,z=>z,w=>w};{x=>x,y=>y,z=>z,w=>w};{x=>x,y=>y,z=>z,w=>w};{x=>x,y=>y,z=>z,w=>w};{x=>x,y=>y,z=>z,w=>w};{x=>x,y=>y,z=>z,w=>w};{x=>x,y=>y,z=>z,w=>w}; a += 1; end
    }
  }
}
