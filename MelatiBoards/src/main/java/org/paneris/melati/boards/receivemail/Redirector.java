package org.paneris.melati.boards.receivemail;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;

class ChannelPair {
  public SocketChannel server = null;
  public SocketChannel client = null;
}
//todo learn how to use Pipes to get more elegant solution in mapping point
public class Redirector extends Thread {

  //todo make more elegant
  //active sessions
  private HashMap client = new HashMap();
  private HashMap server = new HashMap();

  //current server port
  private int port = 119;
  //actual server port
  private int actualPort = 8119;
  //actual server host
  private String host = "localhost";

  public Redirector(int port, int actualPort, String host) {
    this.port = port;
    this.actualPort = actualPort;
    this.host = host;
  }

  private void addPair(SocketChannel client, SocketChannel server) {
    this.client.put(client, server);
    this.server.put(server, client);
  }

  //todo make more elegant :)
  private SocketChannel findPair(SocketChannel other) {
    SocketChannel channel = null;
    if ((channel = (SocketChannel)client.get(other)) != null) {
      return channel;
    }
    if ((channel = (SocketChannel)server.get(other)) != null) {
      return channel;
    }
    return null;
  }

  private void removePair(SocketChannel other) throws IOException {
    SocketChannel channel = null;
    if ((channel = (SocketChannel)client.remove(other)) != null) {
      channel.close();
      server.remove(channel);
    }
    if ((channel = (SocketChannel)server.remove(other)) != null) {
      client.remove(channel);
    }
  }

  public void run() {
    try {
      Selector selector = Selector.open();
      ServerSocketChannel ssc = ServerSocketChannel.open();
      ssc.configureBlocking(false);
      InetSocketAddress isa = new InetSocketAddress(port);
      ssc.socket().bind(isa);
      ssc.register(selector, SelectionKey.OP_ACCEPT);
      while (true) {
        try {
          selector.select();
          Set readyKeys = selector.selectedKeys();
          Iterator i = readyKeys.iterator();
          while (i.hasNext()) {
            SelectionKey sk = (SelectionKey)i.next();
            i.remove();
            Channel nextReady = (Channel)sk.channel();
            if (sk.isAcceptable()) {
              SocketChannel clientChannel =
                ((ServerSocketChannel)nextReady).accept();
              clientChannel.configureBlocking(false);
              SocketChannel serverChannel = null;
              try {
                serverChannel =
                  SocketChannel.open(new InetSocketAddress(host, actualPort));
                serverChannel.configureBlocking(true);
                if (serverChannel.finishConnect()) {
                  serverChannel.configureBlocking(false);
                  clientChannel.register(selector, SelectionKey.OP_READ);
                  serverChannel.register(selector, SelectionKey.OP_READ);
                  addPair(clientChannel, serverChannel);
                }
              } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Cannot connect to local server");
                return;
              }
            }
            if (sk.isReadable()) {
              ByteBuffer buf = ByteBuffer.allocate(512);
              //try to seek in clients map:
              SocketChannel another = findPair((SocketChannel)nextReady);
              int read = ((SocketChannel)nextReady).read(buf);
              if (read > 0) {
                //System.err.println("writing:" + new String(buf.)));
                buf.flip();
                another.write(buf);
              } else if (read == -1) {
                //System.err.println("closing");
                nextReady.close();
                another.close();
                removePair(another);
              }
            }
          }
        } catch (Exception e) {
          //todo
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    int port = 119;
    int actualPort = 8119;
    String host = "localhost";
    if (args.length > 0) {
      if (Pattern.matches("[0-9]+", args[0])) {
        port = Integer.parseInt(args[0]);
      }
    }
    if (args.length > 1) {
      if (Pattern.matches("[0-9]+", args[1])) {
        actualPort = Integer.parseInt(args[1]);
      }
    }
    if(args.length > 2) {
      host = args[2];
    }
    new Redirector(port, actualPort, host).start();
  }

}