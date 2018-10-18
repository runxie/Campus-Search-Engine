from bs4 import BeautifulSoup
from nltk.tokenize import word_tokenize
from nltk.corpus import stopwords
from nltk.stem import SnowballStemmer
from collections import defaultdict

stemmer = SnowballStemmer("english")
import re
import sys
import string
import json
import math
from pymongo import MongoClient

reload(sys)
sys.setdefaultencoding('utf-8')

index = defaultdict(list)
numberOfDocuments = 0

client = MongoClient('localhost', 27017)
db = client.test_database
collection = db.tdIndex


def tokenize(text):
    try:
        text_tokenized = word_tokenize(text)
    except:
        text = unicode(text, errors='ignore')
        text_tokenized = word_tokenize(text)
    return text_tokenized


def parseWordList(tokens):
    tokenlist = []
    for token in tokens:
        try:
            token = token.translate(string.punctuation)
        except:
            token = token
        m = re.match(r'^[a-zA-Z]+$', token)
        if m:
            #if not token in stopwords.words('english'):
            tokenlist.append(stemmer.stem(token).encode('utf-8'))
    return tokenlist


for i in range(75):
    for j in range(10):
        docid = 500 * i + j
        try:
            with open(str(i) + '/' + str(j)) as f:
                soup = BeautifulSoup(f, 'lxml')
            try:
                body = soup.body.text
            except:
                print 'folder', i, 'doc', j, "is not a valid html"
                continue
            for script in soup(["script", "style"]):
                script.decompose()
            fulltext = soup.get_text().encode('utf-8')
            fulltext_tokenized = tokenize(fulltext)
            fulltext_list = parseWordList(fulltext_tokenized)
            numberOfDocuments += 1
	    '''
            try:
                title = soup.title.text
                titlelist = parseWordList(tokenize(title))
            except:
                print 'folder', i, 'doc', j, "does not have a title"
                continue
            '''
            titlelist = []
            
            termdict = {}
	    
            for position, term in enumerate(fulltext_list):
                
                #if not term in stopwords.words('english'):
                    #print term
                    try:
                        #print position
                        termdict[term][1][1].append(position)
                    except:
                        if term in titlelist:
                            termdict[term] = [docid, [[1], [position]]]
                        else:
                            termdict[term] = [docid, [[0], [position]]]
            #print "we are here"
            for term, postingpage in termdict.iteritems():
                index[term].append(postingpage)
        except:
            print 'folder', i, 'doc', j, "It's a not valid html"
            continue

'''{'_word_':{'df':3, '_docid_':{'tf':5, 't': 0/1, 'pos':[5,7]}     }     }'''
docIndex = {}  # doc normalize length

for term, doc in index.iteritems():
    finalindex = {}
    df = len(doc)
    finalindex["_id"] = term
    finalindex['df'] = df
    finalindex["docid"] = {}
    for eachdoc in doc:
        docid = eachdoc[0]
        finalindex["docid"][str(docid)] = {}
        tf = 0
        if eachdoc[1][0][0] == 1:
            tf = 10000 + len(eachdoc[1][1])
            finalindex["docid"][str(docid)]['t'] = 1
        elif eachdoc[1][0][0] == 0:
            tf = len(eachdoc[1][1])
            finalindex["docid"][str(docid)]['t'] = 0
        finalindex["docid"][str(docid)]['tf'] = tf
        finalindex["docid"][str(docid)]['p'] = eachdoc[1][1]
        tfidf = (1 + math.log(tf)) * (math.log(37438.0 / df))
        finalindex["docid"][str(docid)]['tfidf'] = tfidf
        if docid in docIndex:
            docIndex[docid] += tfidf * tfidf
        else:
            docIndex[docid] = tfidf * tfidf
    #print "here"
    collection.insert_one(finalindex)
print "finished part1"
doccollection = db.docInfo

for docid in docIndex:
    record = {}
    record['_id'] = docid
    record['length'] = math.sqrt(docIndex[docid])
    doccollection.insert_one(record)

print "finished part2"

#print 'docindex /n', docIndex


print 'total valid documents', numberOfDocuments

'''
with open('test.json', 'w') as ff:
   json.dump(finalindex, ff, indent=2)


'''
