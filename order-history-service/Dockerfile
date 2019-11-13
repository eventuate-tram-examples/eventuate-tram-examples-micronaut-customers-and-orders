FROM oracle/graalvm-ce
COPY build/libs/order-history-service-0.1.jar .
COPY reflection .
RUN gu install native-image
RUN native-image -cp ./order-history-service-0.1.jar
CMD ./order-history-service-executable