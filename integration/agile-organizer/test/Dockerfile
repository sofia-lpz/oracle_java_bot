FROM selenium/standalone-firefox:latest
WORKDIR /tmp/
COPY test1.py test.py
RUN pip install -U selenium
RUN pip install -U pytest
RUN pip install -U pytest-html
RUN pip install -U webdriver-manager
EXPOSE 8080
RUN pytest test1.py --html=report.html.
ENTRYPOINT ["cat","report.html"]
