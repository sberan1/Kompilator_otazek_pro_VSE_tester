import java.util.List;

class QuestionSet {
    private List<Question> questions;

    public QuestionSet(List<Question> questions) {
        this.questions = questions;
    }

    public List<Question> getQuestions() {
        return questions;
    }
}