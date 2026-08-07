package org.cloudburstmc.protocol.bedrock.definition;

/**
 * A basic registry for protocol definitions that can be expanded upon.
 *
 * @param <D>
 */
public interface DefinitionRegistry<D extends Definition> {

    D getDefinition(int runtimeId);

    /**
     * Looks a definition up by its string identifier.
     *
     * <p>Only registries backed by {@link NamedDefinition} entries can answer this;
     * v2168 sends item descriptors by identifier instead of runtime ID.</p>
     *
     * @param identifier the definition identifier
     * @return the definition, or {@code null} if it is not registered
     * @since v2168
     */
    default D getDefinition(String identifier) {
        throw new UnsupportedOperationException();
    }

    boolean isRegistered(D definition);
}
