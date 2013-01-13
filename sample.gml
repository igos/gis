graph [
        comment "This is a sample graph"
        directed 0
        id 42
        label "Hello, I am a graph"
        node [
                id 1
                label "Node 1"
                thisIsASampleAttribute 42
        ]
        node [
                id 2
                label "node 2"
                thisIsASampleAttribute 43
        ]
        node [
                id 3
                label "node 3"
                thisIsASampleAttribute 44
        ]
        node [
                id 4
                label "node 4"
        ]
        edge [
                source 1
                target 2
                label "Edge from node 1 to node 2"
                weight 3
        ]
        edge [
                source 2
                target 3
                label "Edge from node 2 to node 3"
                weight 4
        ]
        edge [
                source 3
                target 1
                label "Edge from node 3 to node 1"
                weight 5
        ]
        edge [
        		source 3
        		target 4
        		weight 100
        ]
]