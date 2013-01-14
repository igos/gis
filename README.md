gis
===

heuristics for finding local centrals in undirected, weighted graph.


sample run:

GraphGenerator.java -v 1000 -p 0.03 -w 100 -f generated-last.gml

Cli.java -r 20 -f generated-last.gml -t -l 90 -v

run above programs without parameters to see the description.

programs are memory-eaters, so please make sure you provide descen heap size, eg. -Xmx3584m