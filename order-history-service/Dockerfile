FROM oracle/graalvm-ce:19.3.1
COPY build/libs/order-history-service-0.1.jar .
COPY reflection .
RUN gu install native-image
RUN native-image -cp ./order-history-service-0.1.jar

FROM gcc:9.2.0
RUN apt-get install curl
COPY --from=0 /order-history-service-executable .
CMD ./order-history-service-executable
HEALTHCHECK --start-period=30s --interval=5s CMD curl -f http://localhost:8080/health || exit 1