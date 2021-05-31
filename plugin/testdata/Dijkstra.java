public class Dijkstra {

    /**
     * Dijkstra implementation
     *
     * @param s start node index
     * @param n number of nodes
     * @param edges set of edges
     * @return shortest distance from s to each node
     */
    public static int[] dijkstra(int s, int n, Set<Edge> edges) {

        PriorityQueue<Entry> pq = new PriorityQueue<>();

        int[] distances = new int[n];

        // set distances to all nodes to infinity
        for (int i=0; i<n; i++) {
            distances[i] = Integer.MAX_VALUE;
        }

        // distance to start is zero
        distances[s] = 0;

        Entry entry = new Entry(s, 0);

        pq.add(entry);

        while (!pq.isEmpty()) {

            // get closest node
            entry = pq.poll();

            // for each neighbor, if the path going through entry is shorter, update its distance
            for (Edge edge : edges)  {
                if (edge.from == entry.node) {

                    int neighbor = edge.to;

                    int newDist = distances[entry.node] + edge.weight;

                    if (newDist < distances[neighbor]) {
                        distances[neighbor] = newDist;

                        pq.add(new Entry(edge.to, newDist));
                    }

                }
            }
        }

        return distances;
    }


    @Test
    public void testDijkstra() {
        Dijkstra.dijkstra(null,null,null);
    }

}