FROM oracle/graalvm-ce
COPY build/libs/customer-service-0.1-all.jar .
COPY reflection .
RUN gu install native-image
RUN native-image -cp ./customer-service-0.1-all.jar
CMD ./customer-service-executable