MDP4VRP
==========================
A toolkit provides functions for solving time-dependent piecewise polynomial functions and building MDP model.

* Note: this toolkit has not been fully completed, but most functions are ready to use. You can just use it as a package
  for relevant computing works

* Recently I just use Git as a transfer hub (between my computers. :P), so you may see some fragment codes. The structure of this project may change after it release in future.

Basic function includes:

Build graph for VRPs

Assign time-dependent functions to VRP cost functions

Build MDP model from VRP graph

Solve time-dependent MDPs

Do functional computing on Piecewise Polynomial Functions (PPFs), includes:

  For each piece is a determined polynomial function (such as f(x)=c0+c1*x+c2*x^2+...)

    addition (+)

    subtraction (-)

    multiplication (*)

    composition

  For each piece is a stochastic polynomial function (such as f(t,xi)=a*xi+c0+c1*t+c2*t^2+...)

    addition (+)

    subtraction (-)

    multiplication (*)

    composition

    integration on xi(ξ)

Build MDP models

Modularised MDP graphs

Solve MDP

There is a simple demo in the class Test.Test


