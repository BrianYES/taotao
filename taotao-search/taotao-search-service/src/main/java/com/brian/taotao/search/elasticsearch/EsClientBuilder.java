package com.brian.taotao.search.elasticsearch;

import java.net.InetAddress;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

public class EsClientBuilder {

    private Client client;

    public Client init() {
        //设置集群的名字
        Settings settings = Settings.builder()
                .put("client.transport.sniff", false)
                .put("cluster.name", "swmv-es")
                .build();
        //创建集群client并添加集群节点地址
        try {
            client = new PreBuiltTransportClient(settings).addTransportAddresses(new TransportAddress(InetAddress
                .getByName("127.0.0.1"), 9300));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return client;
    }

    public void close() {
        if (client != null) {
            client.close();
        }
    }

    public Client getClient() {
        return client;
    }


}
