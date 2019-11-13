FROM oracle/graalvm-ce
COPY build/libs/order-service-0.1-all.jar .
COPY reflection .
RUN gu install native-image
RUN native-image -cp ./order-service-0.1-all.jar
CMD ./order-service-executable