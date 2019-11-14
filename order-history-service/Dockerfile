FROM oracle/graalvm-ce
COPY build/libs/order-history-service-0.1.jar .
COPY reflection .
RUN gu install native-image
RUN native-image -cp ./order-history-service-0.1.jar
HEALTHCHECK --start-period=30s --interval=5s CMD curl -f http://localhost:8080/health || exit 1
CMD ./order-history-service-executable