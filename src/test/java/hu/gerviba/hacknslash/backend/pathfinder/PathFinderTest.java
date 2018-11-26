package hu.gerviba.hacknslash.backend.pathfinder;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PathFinderTest {

    @Test
    @DisplayName("Coords and IDs")
    void testCoordToId() throws Exception {
        assertEquals(0xF00000008L, PathFinder.coordsToId(0xF, 0x8));
        assertEquals("(15, 8)", PathFinder.idToCoords(0xF00000008L));
        assertEquals(0xBABE, PathFinder.getXofId(0xBABE0000CAFEL));
        assertEquals(0xCAFE, PathFinder.getYofId(0xBABE0000CAFEL));
    }

    @Test
    @DisplayName("Find shortest path (easy)")
    void testFindSPEasy() throws Exception {
        int[][] map = {
                { 0, 0, 1 }, { 1, 0, 1 }, { 2, 0, 1 }, { 3, 0, 1 }, { 4, 0, 1 }, { 5, 0, 1 }, { 6, 0, 1 },
                { 0, 1, 1 }, { 1, 1, 1 }, { 2, 1, 1 }, { 3, 1, 1 }, { 4, 1, 1 }, { 5, 1, 1 }, { 6, 1, 1 },
                { 0, 2, 1 }, { 1, 2, 1 }, { 2, 2, 1 }, { 3, 2, 1 }, { 4, 2, 1 }, { 5, 2, 1 }, { 6, 2, 1 },
                { 0, 3, 1 }, { 1, 3, 1 }, { 2, 3, 1 }, { 3, 3, 0 }, { 4, 3, 0 }, { 5, 3, 1 }, { 6, 3, 0 },
                { 0, 4, 1 }, { 1, 4, 1 }, { 2, 4, 0 }, { 3, 4, 0 }, { 4, 4, 1 }, { 5, 4, 1 }, { 6, 4, 1 },
                { 0, 5, 1 }, { 1, 5, 0 }, { 2, 5, 0 }, { 3, 5, 1 }, { 4, 5, 1 }, { 5, 5, 1 }, { 6, 5, 1 },
                { 0, 6, 1 }, { 1, 6, 1 }, { 2, 6, 0 }, { 3, 6, 1 }, { 4, 6, 1 }, { 5, 6, 1 }, { 6, 6, 1 },
                { 0, 7, 0 }, { 1, 7, 1 }, { 2, 7, 1 }, { 3, 7, 0 }, { 4, 7, 0 }, { 5, 7, 0 }, { 6, 7, 0 },
        };

        List<Long> v = Stream.of(map)
                .filter(coord -> coord[2] == 1)
                .map(coord -> PathFinder.coordsToId(coord[0], coord[1]))
                .collect(Collectors.toList());
        
        PathFinder pf = new PathFinder(v);
        LinkedList<Long> shortestPath = pf.shortestPath(
                PathFinder.coordsToId(1, 1), 
                PathFinder.coordsToId(3, 6));
        
        assertEquals(12, shortestPath.size());
        assertEquals("(1, 1)\n" + 
                "(2, 1)\n" + 
                "(3, 1)\n" + 
                "(4, 1)\n" + 
                "(5, 1)\n" + 
                "(5, 2)\n" + 
                "(5, 3)\n" + 
                "(5, 4)\n" + 
                "(4, 4)\n" + 
                "(4, 5)\n" + 
                "(3, 5)\n" + 
                "(3, 6)", shortestPath.stream()
                .map(PathFinder::idToCoords)
                .collect(Collectors.joining("\n")));
    }

    @Test
    @DisplayName("Find shortest path (no route)")
    void testFindSPImpossible() throws Exception {
        int[][] map = {
                { 0, 0, 1 }, { 1, 0, 1 }, { 2, 0, 1 }, { 3, 0, 1 }, { 4, 0, 1 }, { 5, 0, 1 }, { 6, 0, 1 },
                { 0, 1, 1 }, { 1, 1, 1 }, { 2, 1, 1 }, { 3, 1, 1 }, { 4, 1, 1 }, { 5, 1, 1 }, { 6, 1, 1 },
                { 0, 2, 1 }, { 1, 2, 1 }, { 2, 2, 1 }, { 3, 2, 1 }, { 4, 2, 1 }, { 5, 2, 1 }, { 6, 2, 1 },
                { 0, 3, 1 }, { 1, 3, 1 }, { 2, 3, 1 }, { 3, 3, 0 }, { 4, 3, 0 }, { 5, 3, 0 }, { 6, 3, 0 },
                { 0, 4, 1 }, { 1, 4, 1 }, { 2, 4, 0 }, { 3, 4, 0 }, { 4, 4, 1 }, { 5, 4, 1 }, { 6, 4, 1 },
                { 0, 5, 1 }, { 1, 5, 0 }, { 2, 5, 0 }, { 3, 5, 1 }, { 4, 5, 1 }, { 5, 5, 1 }, { 6, 5, 1 },
                { 0, 6, 1 }, { 1, 6, 1 }, { 2, 6, 0 }, { 3, 6, 1 }, { 4, 6, 1 }, { 5, 6, 1 }, { 6, 6, 1 },
                { 0, 7, 0 }, { 1, 7, 1 }, { 2, 7, 1 }, { 3, 7, 0 }, { 4, 7, 0 }, { 5, 7, 0 }, { 6, 7, 0 },
        };

        List<Long> v = Stream.of(map)
                .filter(coord -> coord[2] == 1)
                .map(coord -> PathFinder.coordsToId(coord[0], coord[1]))
                .collect(Collectors.toList());
        
        PathFinder pf = new PathFinder(v);
        LinkedList<Long> shortestPath = pf.shortestPath(
                PathFinder.coordsToId(1, 1), 
                PathFinder.coordsToId(3, 6));
        
        assertEquals(0, shortestPath.size());
        assertEquals("", shortestPath.stream()
                .map(PathFinder::idToCoords)
                .collect(Collectors.joining("\n")));
    }
    
    @Test
    @DisplayName("Find shortest path (maze)")
    void testFindSPHard() throws Exception {
        int[][] map = {
                { 0, 0, 1 }, { 1, 0, 0 }, { 2, 0, 0 }, { 3, 0, 0 }, { 4, 0, 1 }, { 5, 0, 1 }, { 6, 1, 1 },
                { 0, 1, 1 }, { 1, 1, 1 }, { 2, 1, 1 }, { 3, 1, 0 }, { 4, 1, 1 }, { 5, 1, 1 }, { 6, 1, 1 },
                { 0, 2, 0 }, { 1, 2, 0 }, { 2, 2, 1 }, { 3, 2, 0 }, { 4, 2, 1 }, { 5, 2, 0 }, { 6, 2, 1 },
                { 0, 3, 1 }, { 1, 3, 1 }, { 2, 3, 1 }, { 3, 3, 0 }, { 4, 3, 1 }, { 5, 3, 0 }, { 6, 3, 1 },
                { 0, 4, 1 }, { 1, 4, 1 }, { 2, 4, 0 }, { 3, 4, 0 }, { 4, 4, 0 }, { 5, 4, 0 }, { 6, 4, 1 },
                { 0, 5, 1 }, { 1, 5, 1 }, { 2, 5, 0 }, { 3, 5, 0 }, { 4, 5, 1 }, { 5, 5, 0 }, { 6, 5, 1 },
                { 0, 6, 1 }, { 1, 6, 1 }, { 2, 6, 1 }, { 3, 6, 1 }, { 4, 6, 1 }, { 5, 6, 0 }, { 6, 6, 1 },
                { 0, 7, 0 }, { 1, 7, 0 }, { 2, 7, 0 }, { 3, 7, 0 }, { 4, 7, 1 }, { 5, 7, 1 }, { 6, 7, 1 },
        };

        List<Long> v = Stream.of(map)
                .filter(coord -> coord[2] == 1)
                .map(coord -> PathFinder.coordsToId(coord[0], coord[1]))
                .collect(Collectors.toList());
        
        for (int y = 0; y < 8; ++y) {
            for (int x = 0; x < 7; ++x) {
                System.out.print(map[y * 7 + x][2]);
            }
            System.out.println("");
        }
        System.out.println("");
        
        
        PathFinder pf = new PathFinder(v);
        LinkedList<Long> shortestPath = pf.shortestPath(
                PathFinder.coordsToId(0, 0), 
                PathFinder.coordsToId(4, 3));
        
//        assertEquals(0, shortestPath.size());
//        assertEquals("", shortestPath.stream()
//                .map(PathFinder::idToCoords)
//                .collect(Collectors.joining("\n")));
        
        shortestPath.stream()
                .map(PathFinder::idToCoords)
                .forEach(System.out::println);
        
        for (int y = 0; y < 8; ++y) {
            for (int x = 0; x < 7; ++x) {
                if (shortestPath.contains(PathFinder.coordsToId(x, y)))
                    System.out.print("#");
                else
                    System.out.print(map[y * 7 + x][2]);
            }
            System.out.println("");
        }
    }
    
}
