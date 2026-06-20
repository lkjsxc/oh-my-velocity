package com.ohmyvelocity.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TabRenderServiceTest {
    @Test
    void rendersImportedTabFormatAndGroupOrder() {
        TabConfig config = new TabConfig(
                true,
                5,
                List.of("<gold>{online}/{max}"),
                List.of("<gray>{server}"),
                "§7[%server%]§r %player%",
                List.of("owner", "admin", "default"));
        TabRenderService service = new TabRenderService();

        TabRenderPlan admin = service.render(config, new TabRenderContext("Ada", "hub", "admin", 4, 128, 42, "", ""));
        TabRenderPlan guest = service.render(config, new TabRenderContext("Bob", "hub", "default", 4, 128, 50, "", ""));

        assertEquals("<gray>[hub]<reset> Ada", admin.displayName());
        assertEquals("<gold>4/128", admin.header().getFirst());
        assertTrue(admin.order() < guest.order());
    }
}
