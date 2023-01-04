package com.lwl;

public interface Constant {

    // 交换机
    String CREATE_ORDER_EXCHANGE = "message_driver_test";

    // 队列
    String ORDER_NEW_QUEUE = "order_new";
    String ORDER_NEW_ROUTING_KEY = "order_new";

    String ORDER_LOCKED_QUEUE = "order_locked";
    String ORDER_LOCKED_ROUTING_KEY = "order_locked";

    String ORDER_UNLOCK_QUEUE = "order_unlock";
    String ORDER_UNLOCK_ROUTING_KEY = "order_unlock";


    String ORDER_PAY_ROUTING_KEY = "order_pay";
    String ORDER_PAY_QUEUE = "order_pay";

    String ORDER_TICKET_MOVE_ROUTING_KEY = "order_ticket_move";
    String ORDER_TICKET_MOVE_QUEUE = "order_ticket_move";


    String ORDER_FINISH_QUEUE = "order_finish";
    String ORDER_FINISH_ROUTING_KEY = "order_finish";


    // 索票失败
    String ORDER_FAIL_QUEUE = "order_fail";
    String ORDER_FAIL_ROUTING_KEY = "order_fail";

    //
    String EXEC_ERROR_EXCHANGE = "exec_error";
    String EXEC_ERROR_QUEUE = "exec_error";
    String APPLICATION_JSON = "application/json";

}
