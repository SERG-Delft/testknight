class Methods {

    public void twoBranches(boolean condition) {
        if (condition) {
            System.out.println("true");
        } else {
            System.out.println("false");
        }
    }

    public void switchCase(int param) {

        switch (param) {
            case 0:
                System.out.println("is a zero");
                break;
            case 1:
                System.out.println("is a one");
                break;
            case 2:
                System.out.println("is a two");
                break;
            default:
                System.out.println("is default");
        }
    }

    public int dijkstra() {

        if (t == s) return 0;

        int[] distances = new int[n + 1];
        Entry[] tokens = new Entry[n + 1];
        PriorityQueue<Entry> pq = new PriorityQueue<Entry>();

        // initialize distances
        for (int v=0; v<(n+1); v++) {
            distances[v] = Integer.MAX_VALUE;
        }

        // add number of outgoing edges to cost
        int initialCost = nodes.get(s).size();
        distances[s] = initialCost;

        Entry e = new Entry(s, initialCost);
        pq.add(e);
        tokens[s] = e;

        while (!pq.isEmpty()) {

            Entry entry = pq.poll();

            int vertex = entry.vertex;
            int cost = entry.cost;

            HashMap<Integer, Integer> edges = this.nodes.get(vertex);

            for (int neighbor : edges.keySet()) {

                // recalculated cost
                int newCost = cost + edges.get(neighbor);
                newCost += nodes.get(neighbor).size();

                // if newcost < currentcost relax weights
                if (newCost < distances[neighbor]) {
                    distances[neighbor] = newCost;

                    pq.remove(tokens[neighbor]);

                    Entry relaxedEntry = new Entry(neighbor, newCost);
                    tokens[neighbor] = relaxedEntry;
                    pq.add(relaxedEntry);
                }
            }
        }

        if (distances[t] == Integer.MAX_VALUE) return -1;

        return distances[t] - nodes.get(t).size();
    }

    public void method(boolean condition, boolean second) {

        if (condition) {

            boolean condition = true

            if (condition) {

            }

        }

    }

    public void mcdc() {
        if (a && (b || c)) {
            System.out.println("b1");
        } else {
            System.out.println("b2");
        }
    }

    public void trycatch(int param) {
        try {
            System.out.println("lol");
        } catch(Exception e) {
            throw e
        }
    }

}
