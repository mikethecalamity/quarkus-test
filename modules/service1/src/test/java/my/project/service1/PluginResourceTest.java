package my.project.service1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.stream.Stream;

import javax.enterprise.inject.Instance;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import my.project.plugin.Plugin;
import my.project.service1.PluginResource.Service1Config;

@ExtendWith(MockitoExtension.class)
public class PluginResourceTest {

    @Mock
    private Instance<Plugin> plugins;

    @Mock
    private Service1Config config;

    @InjectMocks
    private PluginResource resource;

    @Test
    public void nameTest() {
        final String expected = "moe";
        when(config.name()).thenReturn(expected);

        final String actual = resource.name();
        assertEquals(expected, actual);
    }

    @Test
    public void pluginsTest() {
        final String expected1 = "larry";
        final Plugin plugin1 = mock(Plugin.class);
        when(plugin1.getName()).thenReturn(expected1);
        when(plugins.stream()).thenReturn(Stream.of(plugin1));

        final List<String> actual = resource.plugins();
        assertTrue(actual.contains(expected1));
    }
}
