package viyper.shale.serialiazation;

import java.util.*;

public class SerializerGraph {
    private final List<Serializer> serializers;

    public SerializerGraph(List<Serializer> serializers) {
        this.serializers = serializers;


    }

    public static void main(String[] args) {
    }

    public Optional<Serializer> serializerFor(Class<?> clazz) {
        //noinspection OptionalGetWithoutIsPresent
        return serializers.stream().filter(serializer -> serializer.serializerPriority(clazz).isPresent())
                          .max(Comparator.comparingInt(serializer -> serializer.serializerPriority(clazz).getAsInt()));
    }

    public SubGraph subGraph(Class<?> clazz) {
        return new SubGraph(new Node(clazz, serializerFor(clazz).get(), new Node.Dependency[0]));
    }

    public enum NodeCylicness {
        UNDETERMINED,
        ACYCLIC,
        WEAK,
        // STRONG, <-- This should invalidate a graph any time it is encountered so it's unnecessary to record it.
    }

    private static class Node {
        final Class<?> clazz;
        final Serializer serializer;
        Dependency[] dependencies;
        Node(Class<?> clazz, Serializer serializer) {
            this.clazz = clazz;
            this.serializer = serializer;
            this.dependencies = new Dependency[0];
        }

        record Dependency(SerializerDependencyLink linkType, Node node) {
        }
    }

    public class SubGraph {
        private Map<Class<?>, Node> nodes = new HashMap<>();

        private SubGraph(Node root) {
            // Fill Sub Graph Nodes
            Stack<Node> stack = new Stack<>();
            stack.add(root);
            while (!stack.empty()) {
                Node node = stack.pop();
                nodes.put(node.clazz, node);

                Serializer.Dependency[] serializerDependencies = node.serializer.dependencies(node.clazz);
                for (Serializer.Dependency serializerDependency : serializerDependencies) {
                    if (nodes.containsKey(serializerDependency.clazz())) continue;
                    Class<?> clazz = serializerDependency.clazz();
                    Optional<Serializer> maybeSerializer = serializerFor(clazz);
                    if (maybeSerializer.isEmpty()) throw new NoApplicableSerializerException(clazz);
                    stack.add(new Node(clazz, maybeSerializer.get()));
                }
            }

            // Place Dependencies
            for (Node node : nodes.values()) {
                node.dependencies = Arrays.stream(node.serializer.dependencies(node.clazz))
                                          .map(dependency -> new Node.Dependency(dependency.linkType(),
                                                                                 nodes.get(dependency.clazz())))
                                          .toArray(
                                                  Node.Dependency[]::new);
            }
        }
    }
}
