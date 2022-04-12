package cn.fhx.sender;

import cn.fhx.common.model.Future;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: fenghaoxian
 * @date: 2022/4/10 21:36
 * @description:
 */
public class GeminiSender {

    private Bootstrap bootstrap;

    private final NioEventLoopGroup worker = new NioEventLoopGroup(2);

    private List<Channel> receiverChannels = new ArrayList<>();

    private final LoggingHandler loggingHandler = new LoggingHandler();

    public void send(Channel channel) {

    }

    public Channel getChannel(String serviceName) {
        if (bootstrap == null){
            initBootStrap();
        }
        // TODO: 2022/4/11 ip地址和端口号应从eureka根据服务名获得
        String ipAddress = "localhost";
        int port = 47651;
        Channel channel = bootstrap.bind(ipAddress, port).syncUninterruptibly().channel();
        receiverChannels.add(channel);
        return channel;
    }

    private void initBootStrap() {
        bootstrap = new Bootstrap()
                .group(worker)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        channel.pipeline()
                                .addLast(loggingHandler);
                    }
                });
    }
}
