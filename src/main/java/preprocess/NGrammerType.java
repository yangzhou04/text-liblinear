package preprocess;

public enum NGrammerType {
    ACCUMULATE, // if n set to 2, then 1gram and 2gram will be used
    NON_ACCUMULATE // if n set to 2, then only 2gram will be used
}
