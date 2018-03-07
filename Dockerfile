FROM        nexus.twdns.top:5000/openjdk:8u151-jre-alpine

ENV         PAYMENT_HOME            /payment
ENV         SERVICE_PATH            $PAYMENT_HOME/payment-service
ENV         PATH                    $PATH:$SERVICE_PATH/bin

RUN         sed -i 's/dl-cdn.alpinelinux.org/mirrors.ustc.edu.cn/g' /etc/apk/repositories \
            && echo https://mirrors.ustc.edu.cn/alpine/edge/testing >> /etc/apk/repositories \
            && apk add --update --no-cache bash \
                ttf-dejavu ttf-droid ttf-freefont ttf-liberation \
                openssl openssl-dev ca-certificates \
            && mkdir -pv ${SERVICE_PATH}/logs \
            && chown -R daemon:daemon ${SERVICE_PATH} \
            && rm -rf /var/cache/apk/*
COPY        --chown=daemon:daemon payment-service-1.0-* $SERVICE_PATH

USER        daemon
EXPOSE      9000
VOLUME      [ "${SERVICE_PATH}/logs" ]
WORKDIR     $SERVICE_PATH
ENTRYPOINT  ["payment-service", "-Dpidfile.path=/dev/null"]
