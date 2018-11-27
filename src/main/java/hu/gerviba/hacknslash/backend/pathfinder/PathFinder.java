package hu.gerviba.hacknslash.backend.pathfinder;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

/**
 * Path finder class using BFS algorithm
 * @author Gergely Szabó
 */
@Slf4j
public class PathFinder {

    private static final long NOT_NEIGHBOUR = -1L;
    private HashMap<Long, LinkedList<Long>> adjacency = new HashMap<>();

    /**
     * List of vertices
     * @param v List of vertices
     */
    public PathFinder(List<Long> v) {
        adjacency = new HashMap<>();
        v.forEach(x -> adjacency.put(x, new LinkedList<>()));
        v.forEach(id -> {
            if (v.contains(coordsToId(getXofId(id) + 1, getYofId(id))))
                addEdge(id, coordsToId(getXofId(id) + 1, getYofId(id)));
            if (v.contains(coordsToId(getXofId(id) - 1, getYofId(id))))
                addEdge(id, coordsToId(getXofId(id) - 1, getYofId(id)));
            if (v.contains(coordsToId(getXofId(id), getYofId(id) + 1)))
                addEdge(id, coordsToId(getXofId(id), getYofId(id) + 1));
            if (v.contains(coordsToId(getXofId(id), getYofId(id) - 1)))
                addEdge(id, coordsToId(getXofId(id), getYofId(id) - 1));
        });
        log.info("A new graph has been built for searching the shortest path");
    }

    /**
     * Connect two verices
     * @param v One vertex
     * @param w To another
     */
    void addEdge(Long v, Long w) {
        adjacency.get(v).add(w);
    }

    /**
     * Apply BFS algorithm
     * @param s Source path
     * @param d Destination path
     * @return The computed pred matrix
     */
    private HashMap<Long, Long> bfs(long s, long d) {
        HashMap<Long, Boolean> visited = new HashMap<>();
        adjacency.keySet().forEach(v -> visited.put(v, false));
        
        HashMap<Long, Integer> dist = new HashMap<>();
        HashMap<Long, Long> pred = new HashMap<>();
        adjacency.keySet().forEach(a -> {
            dist.put(a, Integer.MAX_VALUE);
            pred.put(a, NOT_NEIGHBOUR);
        });

        LinkedList<Long> queue = new LinkedList<>();
        visited.put(s, true);
        dist.put(s, 0);
        queue.add(s);

        while (queue.size() != 0) {
            s = queue.poll();

            for (Long n : adjacency.get(s)) {
                if (!visited.get(n)) {
                    visited.put(n, true);
                    dist.put(n, dist.get(s) + 1); 
                    pred.put(n, s); 
                    queue.add(n);
                    
                    if (n == d)
                        return pred;
                }
            }
        }
        
        return new HashMap<Long, Long>();
    }

    /**
     * Calculate shortest path
     * @param s Source vertex
     * @param d Destination vertex
     * @return List of the shortest path coordinates
     */
    public LinkedList<Long> shortestPath(long s, long d) {
        HashMap<Long, Long> pred = bfs(s, d);
        LinkedList<Long> path = new LinkedList<>(); 
        
        if (!pred.containsKey(d))
            return path;

        long crawl = d; 
        path.addFirst(crawl); 
        while (pred.get(crawl) != NOT_NEIGHBOUR) { 
            path.addFirst(pred.get(crawl)); 
            crawl = pred.get(crawl);
        }

        return path;
    }
    
    /**
     * Converts coordinates to vertex id
     * @param x X coordinate
     * @param y Y coordinate
     * @return converted ID
     */
    public static long coordsToId(int x, int y) {
        return ((long) x & 0x7FFFFFFFL) << 32L | (y & 0x7FFFFFFFL);
    }

    /**
     * Converts ID to coordinates
     * @param id ID of the vertex
     * @return Coordinate string with format: (%d, %d)
     */
    public static String idToCoords(long id) {
        return String.format("(%d, %d)", id >> 32L, id & 0x7FFFFFFFL);
    }

    /**
     * Get X component of the coordinate
     * @param id ID of the vertex
     * @return X coordinate
     */
    public static int getXofId(long id) {
        return (int) (id >> 32L);
    }

    /**
     * Get Y component of the coordinate
     * @param id ID of the vertex
     * @return Y coordinate
     */
    public static int getYofId(long id) {
        return (int) (id & 0x7FFFFFFFL);
    }

}
