package org.cloudburstmc.protocol.bedrock.data.attributelayer;

/**
 * Ortam niteliği geçişindeki gürültü hizalaması.
 *
 * @param type  hizalama türü
 * @param value hizalama değeri
 * @since v2192
 */
public record NoiseAlignment(Type type, int value) {

    public enum Type {
        MIN_LOCAL_TRANSITION_END
    }
}
