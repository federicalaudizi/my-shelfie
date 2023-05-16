package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientSocket;
import it.polimi.ingsw.server.controller.network.Server;
import it.polimi.ingsw.server.controller.network.socket.SocketServer;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class ClientSocketTest {
    public static Client client;
    public static Server server;

    @Before
    public void init() {
        client = new ClientSocket(true);
        server = new SocketServer(5000, new GameSupervisor());
    }

    @Test
    public void validateIpTest() {
        ClientSocket client = new ClientSocket(true);
        try {
            // Prepare private method through Reflections
            Method validateIpMethod = ClientSocket.class.getDeclaredMethod("validateIp", String.class);
            validateIpMethod.setAccessible(true);

            // Check for valid IPs
            assertTrue((boolean) validateIpMethod.invoke(client, "192.168.1.22:23421"));
            assertTrue((boolean) validateIpMethod.invoke(client, "127.0.0.1:5000"));

            // Check for invalid IPs
            assertFalse((boolean) validateIpMethod.invoke(client, "0.0.0.0:00000"));
            assertFalse((boolean) validateIpMethod.invoke(client, "999.999.999.999:99999"));
            assertFalse((boolean) validateIpMethod.invoke(client, "555.134.123.12:42123"));
            assertFalse((boolean) validateIpMethod.invoke(client, "122.999.123.12:42123"));
            assertFalse((boolean) validateIpMethod.invoke(client, "122.134.999.12:42123"));
            assertFalse((boolean) validateIpMethod.invoke(client, "124.134.123.999:42123"));
            assertFalse((boolean) validateIpMethod.invoke(client, "192.134.123.12:65555"));

            // Check exception throwing
            try {
                validateIpMethod.invoke(client, "0");
            } catch (InvocationTargetException e) {
                if(!(e.getCause() instanceof IllegalArgumentException && e.getTargetException().getMessage().equals("Malformed IP string")))
                    fail();
            }
            try {
                validateIpMethod.invoke(client, "0:0:0:0");
            } catch (InvocationTargetException e) {
                if(!(e.getCause() instanceof IllegalArgumentException && e.getTargetException().getMessage().equals("Malformed IP string")))
                    fail();
            }
            try {
                validateIpMethod.invoke(client, "192.168.1.123.123:23423");
            } catch (InvocationTargetException e) {
                if(!(e.getCause() instanceof IllegalArgumentException && e.getTargetException().getMessage().equals("Malformed IP address")))
                    fail();
            }
            try {
                validateIpMethod.invoke(client, "192.157.123.12:abcde");
            } catch (InvocationTargetException e) {
                if(!(e.getCause() instanceof IllegalArgumentException && e.getTargetException().getMessage().equals("Malformed IP port")))
                    fail();
            }
            try {
                validateIpMethod.invoke(client, "abc.168.123.123:19284");
            } catch (InvocationTargetException e) {
                if(!(e.getCause() instanceof IllegalArgumentException && e.getTargetException().getMessage().equals("Malformed IP address segment")))
                    fail();
            }
        } catch (Exception e) {
            fail();
        }
    }
}
